package com.myetherwallet.mewwalletbl.core.api.ws.mew

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.BaseWebSocketClient
import com.myetherwallet.mewwalletbl.core.api.ws.mew.data.Method
import com.myetherwallet.mewwalletbl.core.api.ws.mew.data.MewRequest
import com.myetherwallet.mewwalletbl.core.api.ws.mew.data.MewRequestContent

/**
 * Created by BArtWell on 02.09.2021.
 */

private const val SOCKET_URL = BuildConfig.MEW_MAINNET_SOCKET_END_POINT

class MewWebSocketClient : BaseWebSocketClient(SOCKET_URL, MewParser()) {

    fun <IN, OUT : Any> send(method: Method, path: String, data: IN, clazz: Class<OUT>, callback: (Either<Failure.WebSocketError, OUT>) -> Unit) {
        val id = getNextId()
        return sendForResponse(id, MewRequest(MewRequestContent(id, method.getMethod(), path, data)), clazz, callback)
    }
}
