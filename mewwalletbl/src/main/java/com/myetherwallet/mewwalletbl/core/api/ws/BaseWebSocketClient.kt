package com.myetherwallet.mewwalletbl.core.api.ws

import android.util.SparseArray
import com.myetherwallet.mewwalletbl.connection.WebSocketWrapper
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.data.BaseRequest
import com.myetherwallet.mewwalletbl.core.api.ws.data.Chain
import com.myetherwallet.mewwalletbl.core.json.JsonParser
import org.json.JSONObject

/**
 * Created by BArtWell on 15.02.2022.
 */

private const val TAG = "BaseWebSocketClient"

abstract class BaseWebSocketClient(private val url: String, private val parser: Parser) {

    @Volatile
    private var id = 1
    private val webSocketWrapper = WebSocketWrapper()

    private val requestCallbacks = SparseArray<Chain<*>>()

    fun connect() {
        webSocketWrapper.connect(url)
        webSocketWrapper.onMessageListener = ::onMessageReceived
    }

    private fun onMessageReceived(data: String) {
        val responseId = getResponseId(data)
        if (responseId != null && requestCallbacks[responseId] != null) {
            requestCallbacks[responseId].setResult(data)
        } else {
            handleEvent(data)
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

    protected fun getNextId() = id++

    protected fun <OUT : Any> sendForResponse(id: Int, request: BaseRequest, clazz: Class<OUT>, callback: (Either<Failure.WebSocketError, OUT>) -> Unit) {
        val chain = Chain(request, parser, clazz, callback)
        requestCallbacks.put(id, chain)
        send(chain.request)
    }

    private fun <T> send(data: T) {
        val json = JsonParser.toJson(data)
        webSocketWrapper.send(json)
    }

    protected open fun handleEvent(data: String) {}

    fun disconnect() {
        webSocketWrapper.disconnect()
        requestCallbacks.clear()
        id = 0
    }
}
