package com.myetherwallet.mewwalletbl.connection

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import androidx.core.content.ContextCompat
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.connection.webrtc.WebRtc
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.json.JsonParser
import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by BArtWell on 23.07.2019.
 */

private const val TAG = "SocketService"

private const val ACTION_START = "${BuildConfig.LIBRARY_PACKAGE_NAME}.$TAG.ACTION_START"
private const val ACTION_STOP = "${BuildConfig.LIBRARY_PACKAGE_NAME}.$TAG.ACTION_STOP"

private const val INCOME_CONFIRMATION = "confirmation"
private const val INCOME_OFFER = "offer"
private const val INCOME_ATTEMPTING_TURN = "attemptingturn"
private const val INCOME_DISCONNECT = "disconnect"

private const val OUTCOME_ANSWER_SIGNAL = "answersignal"
private const val OUTCOME_RTC_CONNECTED = "rtcconnected"

class MewConnectService : Service() {

    companion object {

        private val CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(10)

        fun getIntent(context: Context) = Intent(context, MewConnectService::class.java)

        fun start(context: Context) {
            MewLog.d(TAG, "Start")
            val intent = getIntent(context)
            intent.action = ACTION_START
            ContextCompat.startForegroundService(context, intent)
        }

        fun stop(context: Context) {
            MewLog.d(TAG, "Stop")
            val intent = getIntent(context)
            intent.action = ACTION_STOP
            ContextCompat.startForegroundService(context, intent)
        }
    }

    var isConnected = false
    private val binder = ServiceBinder(this)
    private val handler = Handler()
    private var socket: WebSocketWrapper? = null
    private lateinit var messageCrypt: MessageCrypt
    private lateinit var privateKey: String
    private lateinit var connectionId: String
    private lateinit var walletAddress: Address
    private var webRtc: WebRtc? = null

    private var timeoutRunnable: Runnable? = null
    private var wasTurnUsed = false
    private var turnServers: List<TurnServer>? = null
    private var isDisconnectSignalReceived = false

    var errorListener: (() -> Unit)? = null
    var connectedListener: (() -> Unit)? = null
    var connectingListener: (() -> Unit)? = null
    var transactionConfirmListener: ((address: String, transaction: Transaction) -> Unit)? = null
    var messageSignListener: ((address: String, message: MessageToSign) -> Unit)? = null
    var disconnectListener: (() -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        val notificationHelper = ServiceNotificationHelper()
        startForeground(1, notificationHelper.create(this))
    }

    override fun onBind(intent: Intent) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            MewLog.d(TAG, "Stop received")
            disconnect()
            disconnectListener?.invoke()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    MewLog.d(TAG, "Service stop delay fired")
                    stopService()
                }
            }, 500L)
        }
        return START_NOT_STICKY
    }

    fun connect(privateKey: String, connectionId: String, walletAddress: String) {
        MewLog.d(TAG, "Connect")
        MewLog.d(TAG, "PrivateKey $privateKey")
        disconnect()
        wasTurnUsed = false
        isDisconnectSignalReceived = false
        turnServers = null
        connectingListener?.invoke()
        this.privateKey = privateKey
        val ethereumAddress = Address.createEthereum(walletAddress)
        if (ethereumAddress == null) {
            MewLog.e(TAG, "Wrong address")
            errorListener?.invoke()
        } else {
            this.walletAddress = ethereumAddress
            this.connectionId = connectionId
            messageCrypt = MessageCrypt(this.privateKey)
            val url = "${BuildConfig.SOCKET_END_POINT}?role=receiver&connId=$connectionId&signed=" + messageCrypt.signMessage(privateKey)
            socket = WebSocketWrapper().apply {
                onConnectedListener = ::onConnected
                onErrorListener = ::onError
                onDisconnectedListener = ::onDisconnected
                onMessageListener = ::onMessage
                connect(url)
            }
            startTimeoutTimer()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onConnected() {
        MewLog.d(TAG, "onConnected")
    }

    private fun onMessage(text: String) {
        MewLog.showDebugToast(this, "onMessage $text")
        MewLog.d(TAG, "onMessage: $text")
        val message = JsonParser.fromJson(text, SignalMessage::class.java)
        when (message.signal) {
            INCOME_CONFIRMATION -> {
                MewLog.d(TAG, "Receive confirmation, creating WebRTC")
                connectWebRtc(null)
                startTimeoutTimer()
            }
            INCOME_OFFER -> {
                MewLog.d(TAG, "Receive offer, preparing answer")
                val data = JsonParser.fromJson(message.data, SignalData::class.java)
                val decrypted = messageCrypt.decrypt(data.data)?.let { String(it) }
                if (decrypted == null) {
                    onError("Cannot decrypt offer")
                } else {
                    val offer = JsonParser.fromJson(decrypted, Offer::class.java)
                    val sessionDescription = offer.toSessionDescription()
                    webRtc?.setOffer(sessionDescription)
                }
                startTimeoutTimer()
            }
            INCOME_ATTEMPTING_TURN -> {
                MewLog.d(TAG, "Receive turn")
                disconnect(false)
                val data = JsonParser.fromJson(message.data, TurnServerData::class.java)
                turnServers = data.data
                connectWebRtc(turnServers)
            }
            INCOME_DISCONNECT -> {
                MewLog.d(TAG, "Receive disconnect signal")
                isDisconnectSignalReceived = true
                disconnectSocket()
            }
        }
    }

    private fun connectWebRtc(turnServers: List<TurnServer>?) {
        webRtc = WebRtc().apply {
            disconnect()
            answerListener = ::onAnswerCreated
            connectSuccessListener = ::onWebRtcConnected
            messageListener = { handleWebRtcMessages(it) }
            connectErrorListener = ::onWebRtcConnectError
            disconnectListener = ::onRtcDisconnected
            connect(this@MewConnectService, turnServers)
        }
    }

    private fun onAnswerCreated(offer: Offer) {
        MewLog.showDebugToast(this, "Send answer")
        MewLog.d(TAG, "Send answer")
        socket?.send(JsonParser.toJson(SignalAction(OUTCOME_ANSWER_SIGNAL, SignalData(connectionId, messageCrypt.encrypt(offer.toByteArray())))))
        startTimeoutTimer()
    }

    private fun onWebRtcConnected() {
        MewLog.d(TAG, "WebRTC connected")
        MewLog.showDebugToast(this, "Connected")
        socket?.send(JsonParser.toJson(SignalAction(OUTCOME_RTC_CONNECTED, null)))
        startTimeoutTimer()
    }

    private fun onWebRtcConnectError() {
        MewLog.d(TAG, "onWebRtcConnectError")
        waitForTurnOrQuit()
    }

    private fun onRtcDisconnected() {
        MewLog.d(TAG, "onRtcDisconnected")
        disconnect()
        disconnectListener?.invoke()
        stopService()
    }

    private fun onRtcDataOpened() {
        MewLog.d(TAG, "onRtcDataOpened")
        connectedListener?.invoke()
        isConnected = true
        stopTimeoutTimer()
    }

    private fun waitForTurnOrQuit() {
        if (wasTurnUsed) {
            disconnect()
            errorListener?.invoke()
        } else {
            wasTurnUsed = true
            if (turnServers == null) {
                startTimeoutTimer()
                MewLog.d(TAG, "Waiting for turn servers")
            } else {
                MewLog.d(TAG, "Turn servers already received")
            }
        }
    }

    private fun handleWebRtcMessages(json: String) {
        MewLog.d(TAG, "handleWebRtcMessages $json")
        if (!isConnected) {
            onRtcDataOpened()
        }
        try {
            val encryptedMessage = JsonParser.fromJson(json, EncryptedMessage::class.java)
            val webRtcMessage = JsonParser.fromJson<WebRtcMessage<JsonElement>>(
                messageCrypt.decrypt(encryptedMessage)!!,
                object : TypeToken<WebRtcMessage<JsonElement>>() {}.type
            )
            MewLog.d(TAG, webRtcMessage.type.name)
            MewLog.d(TAG, webRtcMessage.data.toString())
            when (webRtcMessage.type) {
                WebRtcMessage.Type.ADDRESS -> {
                    val message = messageCrypt.encrypt(
                        WebRtcMessage(WebRtcMessage.Type.ADDRESS, walletAddress)
                    )
                    webRtc?.send(message)
                }
                WebRtcMessage.Type.SIGN_TX -> {
                    val transaction = JsonParser.fromJson(webRtcMessage.data.asString as String, Transaction::class.java)
                    transactionConfirmListener?.invoke(walletAddress.address, transaction)
                }
                WebRtcMessage.Type.SIGN_MESSAGE -> {
                    messageSignListener?.invoke(walletAddress.address, JsonParser.fromJson(webRtcMessage.data, MessageToSign::class.java))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendSignTx(signedMessage: ByteArray) {
        try {
            val message = WebRtcMessage(WebRtcMessage.Type.SIGN_TX, signedMessage.toHexString().toLowerCase(Locale.US))
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(signature: String) {
        try {
            val message = WebRtcMessage(WebRtcMessage.Type.SIGN_MESSAGE, MessageSignData(walletAddress.address, signature))
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onError(error: String?) {
        MewLog.e(TAG, "onError: $error")
        MewLog.showDebugToast(this, "Error: $error")
        disconnect()
        errorListener?.invoke()
        stopService()
    }

    private fun onDisconnected() {
        MewLog.d(TAG, "onDisconnected")
        if (!isDisconnectSignalReceived) {
            disconnectListener?.invoke()
            stopService()
        }
    }

    private fun disconnect(closeSocket: Boolean = true) {
        MewLog.d(TAG, "disconnect")
        isConnected = false
        ServiceAlarmReceiver.cancel(this)
        try {
            stopTimeoutTimer()
            webRtc?.disconnect()
            if (closeSocket) {
                disconnectSocket()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun disconnectSocket() {
        MewLog.d(TAG, "Close socket")
        socket?.disconnect()
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        MewLog.d(TAG, "onDestroy")
        disconnect()
        super.onDestroy()
    }

    private fun startTimeoutTimer() {
        stopTimeoutTimer()
        MewLog.d(TAG, "Start timer")
        timeoutRunnable = Runnable {
            MewLog.d(TAG, "Timeout")
            waitForTurnOrQuit()
        }
        handler.postDelayed(timeoutRunnable!!, CONNECT_TIMEOUT)
    }

    private fun stopTimeoutTimer() {
        MewLog.d(TAG, "Stop timer")
        timeoutRunnable?.let {
            handler.removeCallbacks(it)
            timeoutRunnable = null
        }
    }
}
