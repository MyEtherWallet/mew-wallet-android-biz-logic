package com.myetherwallet.mewwalletbl.connection

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.R
import com.myetherwallet.mewwalletbl.connection.webrtc.WebRtc
import com.myetherwallet.mewwalletbl.core.LogsCollector
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.json.JsonParser
import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.util.NotificationBuilder
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.eip.eip155.LegacyTransaction
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import com.myetherwallet.mewwalletkit.eip.eip1559.Eip1559Transaction
import okhttp3.Response
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by BArtWell on 23.07.2019.
 */

private const val TAG = "MewConnectService"

private val CONNECT_TIMEOUT = TimeUnit.SECONDS.toMillis(10)

private const val ACTION_START = "${BuildConfig.LIBRARY_PACKAGE_NAME}.$TAG.ACTION_START"
private const val ACTION_STOP = "${BuildConfig.LIBRARY_PACKAGE_NAME}.$TAG.ACTION_STOP"

private const val INCOME_CONFIRMATION = "confirmation"
private const val INCOME_OFFER = "offer"
private const val INCOME_ATTEMPTING_TURN = "attemptingturn"
private const val INCOME_DISCONNECT = "disconnect"

private const val OUTCOME_ANSWER_SIGNAL = "answersignal"
private const val OUTCOME_RTC_CONNECTED = "rtcconnected"

private const val WEB_SOCKET_QR_EXPIRED_CODE = 404

class MewConnectService : Service() {

    private val binder = ServiceBinder(this)
    private val logsCollector = LogsCollector()
    private val handler = Handler(Looper.getMainLooper())
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

    var errorListener: ((LogsCollector) -> Unit)? = null
    var qrExpiredListener: (() -> Unit)? = null
    var connectedListener: (() -> Unit)? = null
    var connectingListener: (() -> Unit)? = null
    var transactionConfirmListener: ((address: String, transaction: Transaction) -> Unit)? = null
    var messageSignListener: ((address: String, message: MessageToSign) -> Unit)? = null
    var getEncryptionPublicKeyListener: (() -> Unit)? = null
    var decryptListener: ((String) -> Unit)? = null
    var signTypedDataV3V4Listener: ((WebRtcMessage.Type, String) -> Unit)? = null
    var disconnectListener: (() -> Unit)? = null

    override fun onCreate() {
        super.onCreate()
        val notificationId = hashCode()
        val notification = NotificationBuilder()
            .setNotificationId(notificationId)
            .setChannelId("com.myetherwallet.mewwalletbl.connection.socket_service")
            .setChannelName(this, R.string.service_notification_channel_name)
            .setChannelDescription(this, R.string.service_notification_channel_description)
            .setTitle(this, R.string.service_notification_title)
            .setText(this, R.string.service_notification_text)
            .setColor(this, R.color.service_notification_icon_background)
            .setSmallIcon(R.drawable.service_notification)
            .setContentIntentToLauncherActivity(this)
            .build(this)
        startForeground(notificationId, notification)
    }

    override fun onBind(intent: Intent) = binder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            logsCollector.add(TAG, "Stop received")
            disconnect()
            disconnectListener?.invoke()
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    logsCollector.add(TAG, "Service stop delay fired")
                    stopService()
                }
            }, 500L)
        }
        return START_NOT_STICKY
    }

    fun connect(privateKey: String, connectionId: String, walletAddress: String) {
        logsCollector.add(TAG, "Connect (id=$connectionId)")
        MewLog.d(TAG, "PrivateKey $privateKey")
        if (isConnected) {
            logsCollector.add(TAG, "Already connected, ignore")
            return
        }
        disconnect()
        wasTurnUsed = false
        isDisconnectSignalReceived = false
        turnServers = null
        connectingListener?.invoke()
        this.privateKey = privateKey
        val ethereumAddress = Address.createEthereum(walletAddress)
        if (ethereumAddress == null) {
            logsCollector.add(TAG, "Wrong address")
            onError("Wrong address")
        } else {
            this.walletAddress = ethereumAddress
            this.connectionId = connectionId
            messageCrypt = MessageCrypt(this.privateKey)
            val url = "${BuildConfig.MEW_CONNECT_SOCKET_END_POINT}?role=receiver&connId=$connectionId&signed=" + messageCrypt.signMessage(privateKey)
            socket = WebSocketWrapper().apply {
                onConnectedListener = ::onSocketConnected
                onErrorListener = ::onSocketError
                onDisconnectedListener = ::onSocketDisconnected
                onMessageListener = ::onSocketMessage
                connect(url)
            }
            startTimeoutTimer()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun onSocketConnected() {
        logsCollector.add(TAG, "onSocketConnected")
    }

    private fun onSocketMessage(text: String) {
        MewLog.showDebugToast(this, "onMessage $text")
        logsCollector.add(TAG, "onMessage")
        MewLog.d(TAG, "onMessage: $text")
        val message = JsonParser.fromJson(text, SignalMessage::class.java)
        when (message.signal) {
            INCOME_CONFIRMATION -> {
                logsCollector.add(TAG, "Receive confirmation, creating WebRTC")
                connectWebRtc(null)
                startTimeoutTimer()
            }
            INCOME_OFFER -> {
                logsCollector.add(TAG, "Receive offer, preparing answer")
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
                logsCollector.add(TAG, "Receive turn")
                disconnect(false)
                val data = JsonParser.fromJson(message.data, TurnServerData::class.java)
                turnServers = data.data
                connectWebRtc(turnServers)
            }
            INCOME_DISCONNECT -> {
                logsCollector.add(TAG, "Receive disconnect signal")
                isDisconnectSignalReceived = true
                disconnectSocket()
            }
        }
    }

    private fun connectWebRtc(turnServers: List<TurnServer>?) {
        logsCollector.add(TAG, "Turn servers count: " + turnServers?.size)
        webRtc = WebRtc(logsCollector).apply {
            disconnect()
            answerListener = ::onAnswerCreated
            connectSuccessListener = ::onWebRtcConnected
            messageListener = { handleWebRtcMessages(it) }
            connectErrorListener = ::onWebRtcConnectError
            disconnectListener = ::onWebRtcDisconnected
            connect(this@MewConnectService, turnServers)
        }
    }

    private fun onAnswerCreated(offer: Offer) {
        MewLog.showDebugToast(this, "Send answer")
        logsCollector.add(TAG, "Send answer")
        socket?.send(JsonParser.toJson(SignalAction(OUTCOME_ANSWER_SIGNAL, SignalData(connectionId, messageCrypt.encrypt(offer.toByteArray())))))
        startTimeoutTimer()
    }

    private fun onWebRtcConnected() {
        logsCollector.add(TAG, "WebRTC connected")
        MewLog.showDebugToast(this, "Connected")
        socket?.send(JsonParser.toJson(SignalAction(OUTCOME_RTC_CONNECTED, null)))
        startTimeoutTimer()
    }

    private fun onWebRtcConnectError(throwable: Throwable) {
        logsCollector.add(TAG, "WebRTC connection error", throwable)
        waitForTurnOrQuit("WebRTC connect error")
    }

    private fun onWebRtcDisconnected() {
        logsCollector.add(TAG, "WebRTC disconnected")
        disconnect()
        disconnectListener?.invoke()
        stopService()
    }

    private fun onRtcDataOpened() {
        logsCollector.add(TAG, "onRtcDataOpened")
        connectedListener?.invoke()
        isConnected = true
        stopTimeoutTimer()
    }

    private fun waitForTurnOrQuit(error: String) {
        if (wasTurnUsed) {
            onError(error)
        } else {
            wasTurnUsed = true
            if (turnServers == null) {
                disconnect(false)
                startTimeoutTimer()
                logsCollector.add(TAG, "Waiting for turn servers")
            } else {
                logsCollector.add(TAG, "Turn servers already received")
            }
        }
    }

    private fun handleWebRtcMessages(json: String) {
        logsCollector.add(TAG, "handleWebRtcMessages")
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
            logsCollector.add(TAG, "Message type: " + webRtcMessage.type?.name)
            MewLog.d(TAG, webRtcMessage.data.toString())
            when (webRtcMessage.type) {
                WebRtcMessage.Type.ADDRESS -> {
                    val message = messageCrypt.encrypt(
                        WebRtcMessage(WebRtcMessage.Type.ADDRESS, walletAddress)
                    )
                    webRtc?.send(message)
                }
                WebRtcMessage.Type.SIGN_TX -> {
                    val jsonString = webRtcMessage.data.asString
                    val transaction = when {
                        jsonString.contains("maxFeePerGas") && jsonString.contains("maxPriorityFeePerGas") ->
                            JsonParser.fromJson(jsonString, Eip1559Transaction::class.java).apply { eipType = Transaction.EIPTransactionType.EIP1559 }
                        else -> JsonParser.fromJson(jsonString, LegacyTransaction::class.java).apply { eipType = Transaction.EIPTransactionType.LEGACY }
                    }
                    transactionConfirmListener?.invoke(walletAddress.address, transaction)
                }
                WebRtcMessage.Type.SIGN_MESSAGE -> {
                    messageSignListener?.invoke(walletAddress.address, JsonParser.fromJson(webRtcMessage.data, MessageToSign::class.java))
                }
                WebRtcMessage.Type.GET_ENCRYPTION_PUBLIC_KEY -> {
                    getEncryptionPublicKeyListener?.invoke()
                }
                WebRtcMessage.Type.DECRYPT -> {
                    decryptListener?.invoke(webRtcMessage.data.asJsonArray[0].asString)
                }
                WebRtcMessage.Type.SIGN_TYPED_DATA_V3, WebRtcMessage.Type.SIGN_TYPED_DATA_V4 -> {
                    signTypedDataV3V4Listener?.invoke(webRtcMessage.type, webRtcMessage.data.asString as String)
                }
                WebRtcMessage.Type.REJECT -> {}
                null -> {}
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendSignTx(data: ByteArray) {
        try {
            val signedMessage = data.toHexString().lowercase(Locale.US)
            logsCollector.add(TAG, "sendSignTx")
            MewLog.d(TAG, "sendSignTx $signedMessage")
            val message = WebRtcMessage(WebRtcMessage.Type.SIGN_TX, signedMessage)
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendMessage(signature: String) {
        try {
            logsCollector.add(TAG, "sendMessage")
            MewLog.d(TAG, "sendMessage $signature")
            val message = WebRtcMessage(WebRtcMessage.Type.SIGN_MESSAGE, MessageSignData(walletAddress.address, signature))
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendTypedMessageV3V4(type: WebRtcMessage.Type, signature: String) {
        try {
            logsCollector.add(TAG, "sendTypedMessageV3V4")
            MewLog.d(TAG, "sendTypedMessageV3V4 $signature")
            val message = WebRtcMessage(type, signature)
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendReject() {
        try {
            logsCollector.add(TAG, "sendReject")
            MewLog.d(TAG, "sendReject $connectionId")
            val message = WebRtcMessage(WebRtcMessage.Type.REJECT, RejectSign(connectionId))
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendEncryptionPublicKey(publicKey: String) {
        try {
            logsCollector.add(TAG, "sendEncryptionPublicKey")
            MewLog.d(TAG, "sendEncryptionPublicKey $publicKey")
            val message = WebRtcMessage(WebRtcMessage.Type.GET_ENCRYPTION_PUBLIC_KEY, publicKey)
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendDecrypted(decrypted: String) {
        try {
            logsCollector.add(TAG, "sendDecrypted")
            MewLog.d(TAG, "sendDecrypted $decrypted")
            val message = WebRtcMessage(WebRtcMessage.Type.DECRYPT, decrypted)
            webRtc?.send(messageCrypt.encrypt(message))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onSocketError(throwable: Throwable?, response: Response?) {
        logsCollector.add(TAG, "onSocketError", throwable)
        if (response?.code == WEB_SOCKET_QR_EXPIRED_CODE) {
            logsCollector.add(TAG, "QR expired")
            disconnect()
            qrExpiredListener?.invoke()
            stopService()
        } else {
            onError(throwable?.message)
        }
    }

    private fun onError(error: String?) {
        logsCollector.add(TAG, "onError: $error")
        MewLog.showDebugToast(this, "Error: $error")
        disconnect()
        errorListener?.invoke(logsCollector)
        stopService()
    }

    private fun onSocketDisconnected() {
        logsCollector.add(TAG, "WebSocket disconnected")
        if (!isDisconnectSignalReceived) {
            disconnectListener?.invoke()
            stopService()
        }
    }

    private fun disconnect(closeSocket: Boolean = true) {
        logsCollector.add(TAG, "disconnect")
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
        logsCollector.add(TAG, "Close socket")
        socket?.disconnect()
    }

    private fun stopService() {
        logsCollector.add(TAG, "Stop service")
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        logsCollector.add(TAG, "onDestroy")
        disconnect()
        super.onDestroy()
    }

    private fun startTimeoutTimer() {
        stopTimeoutTimer()
        logsCollector.add(TAG, "Start timer")
        timeoutRunnable = Runnable {
            logsCollector.add(TAG, "Timeout")
            waitForTurnOrQuit("Timeout")
        }
        handler.postDelayed(timeoutRunnable!!, CONNECT_TIMEOUT)
    }

    private fun stopTimeoutTimer() {
        logsCollector.add(TAG, "Stop timer")
        timeoutRunnable?.let {
            handler.removeCallbacks(it)
            timeoutRunnable = null
        }
    }

    companion object {

        var isConnected = false

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
}
