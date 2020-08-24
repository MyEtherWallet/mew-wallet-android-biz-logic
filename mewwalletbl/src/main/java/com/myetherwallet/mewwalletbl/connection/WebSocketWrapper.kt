package com.myetherwallet.mewwalletbl.connection

import com.myetherwallet.mewwalletbl.core.MewLog
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okio.ByteString
import java.util.concurrent.TimeUnit

/**
 * Created by BArtWell on 02.08.2019.
 */

private const val TAG = "WebSocketWrapper"

class WebSocketWrapper {

    var onConnectedListener: (() -> Unit)? = null
    var onErrorListener: ((Throwable?, Response?) -> Unit)? = null
    var onDisconnectedListener: (() -> Unit)? = null
    var onMessageListener: ((message: String) -> Unit)? = null

    private var webSocket: WebSocket? = null

    fun connect(url: String) {
        MewLog.d(TAG, "Connect: $url")
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                this@WebSocketWrapper.webSocket = webSocket
                onConnectedListener?.invoke()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                MewLog.d(TAG, "String received")
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

    fun send(command: String) {
        MewLog.d(TAG, "Sent: $command")
        webSocket?.send(command)
    }

    fun disconnect() {
        MewLog.d(TAG, "Disconnect")
        webSocket?.close(1000, null)
    }
}
