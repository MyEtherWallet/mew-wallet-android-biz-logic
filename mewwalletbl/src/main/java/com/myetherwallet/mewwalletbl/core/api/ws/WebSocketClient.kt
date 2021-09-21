package com.myetherwallet.mewwalletbl.core.api.ws

import android.util.SparseArray
import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.connection.WebSocketWrapper
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.data.Chain
import com.myetherwallet.mewwalletbl.core.api.ws.data.Method
import com.myetherwallet.mewwalletbl.core.api.ws.data.Request
import com.myetherwallet.mewwalletbl.core.api.ws.data.RequestData
import com.myetherwallet.mewwalletbl.core.json.JsonParser
import org.json.JSONObject

/**
 * Created by BArtWell on 02.09.2021.
 */

private const val TAG = "WebSocketClient"
private const val URL = BuildConfig.MEW_MAINNET_SOCKET_END_POINT

class WebSocketClient {

    @Volatile
    private var id = 0
    private val webSocketWrapper = WebSocketWrapper()

    private val requestCallbacks = SparseArray<Chain<*, *>>()
    private val eventCallback: (() -> Unit)? = null

    fun connect() {
        webSocketWrapper.connect(URL)
        webSocketWrapper.onMessageListener = ::onMessageReceived
    }

    private fun onMessageReceived(data: String) {
        val responseId = getResponseId(data)
        if (responseId != null && requestCallbacks[responseId] != null) {
            requestCallbacks[responseId].setResult(data)
        } else {
            eventCallback?.invoke()
        }
    }

    private fun getResponseId(data: String): Int? {
        try {
            val jsonObject = JSONObject(data)
            if (jsonObject.has("id")) {
                return jsonObject.getInt("id")
            }
        } catch (e: Exception) {
            MewLog.e(TAG, "Can't parse response", e)
        }
        return null
    }

    fun <IN, OUT : Any> send(method: Method, path: String, data: IN, clazz: Class<OUT>, callback: (Either<Failure.WebSocketError, OUT>) -> Unit) {
        val id = this.id++
        val chain = Chain(Request(RequestData(id, method.getMethod(), path, data)), clazz, callback)
        requestCallbacks.put(id, chain)
        send(chain.request)
    }

    private fun <T> send(data: T) {
        val json = JsonParser.toJson(data)
        webSocketWrapper.send(json)
    }

//    fun <T> listen(event: Event, callback: (T) -> Unit) {
//        appViewModel.observe(owner, KEY_LISTEN + event.name, callback)
//    }

    fun disconnect() {
        webSocketWrapper.disconnect()
    }

    companion object {

        var client: WebSocketClient? = null

        fun connect() {
            client = WebSocketClient()
            client?.connect()
        }

        fun disconnect() {
            client?.disconnect()
            client = null
        }
    }
}
