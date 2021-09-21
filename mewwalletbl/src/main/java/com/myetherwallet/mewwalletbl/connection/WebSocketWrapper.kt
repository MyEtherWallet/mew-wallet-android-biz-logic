package com.myetherwallet.mewwalletbl.connection

import com.myetherwallet.mewwalletbl.core.MewLog
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by BArtWell on 02.08.2019.
 */

private const val TAG = "WebSocketWrapper"
private val SERVER_READY_DELAY = TimeUnit.SECONDS.toMillis(1)
private const val TIMEOUT = 120L
private const val PING_PERIOD = 30L

class WebSocketWrapper {

    var onConnectedListener: (() -> Unit)? = null
    var onErrorListener: ((Throwable?, Response?) -> Unit)? = null
    var onDisconnectedListener: (() -> Unit)? = null
    var onMessageListener: ((message: String) -> Unit)? = null

    private var webSocket: WebSocket? = null
    private var isConnected = false
    private val suspendedSends = mutableListOf<String>()

    fun connect(url: String) {
        MewLog.d(TAG, "Connect: $url")
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .pingInterval(PING_PERIOD, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                if (response.isSuccessful || response.code == 101) {
                    MewLog.e(TAG, "Opened (" + response.code + ": " + response.message + ")")
                    // Wait a second while server ready after connecting
                    Timer().schedule(object : TimerTask() {
                        override fun run() {
                            isConnected = true
                            flushSuspended()
                        }
                    }, SERVER_READY_DELAY)
                } else {
                    MewLog.e(TAG, "Open error [" + response.code + "]: " + response.message)
                }
                this@WebSocketWrapper.webSocket = webSocket
                onConnectedListener?.invoke()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                MewLog.d(TAG, "String received: $text")
                onMessageListener?.invoke(text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                MewLog.d(TAG, "Bytes received")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                MewLog.d(TAG, "Close connection: $code $reason")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                MewLog.d(TAG, "Close connection: $code $reason")
                onDisconnectedListener?.invoke()
            }

            override fun onFailure(webSocket: WebSocket, throwable: Throwable, response: Response?) {
                MewLog.d(TAG, "Error", throwable)
                onErrorListener?.invoke(throwable, response)
            }
        })
        client.dispatcher.executorService.shutdown()
    }

    private fun flushSuspended() {
        MewLog.d(TAG, "Flush")
        while (suspendedSends.isNotEmpty()) {
            send(suspendedSends[0])
            suspendedSends.removeAt(0)
        }
    }

    fun send(command: String) {
        if (isConnected) {
            MewLog.d(TAG, "Sent: $command")
            webSocket?.send(command)
        } else {
            MewLog.d(TAG, "Save: $command")
            suspendedSends.add(command)
        }
    }

    fun disconnect() {
        MewLog.d(TAG, "Disconnect")
        webSocket?.close(1000, null)
        isConnected = false
    }
}
