package com.myetherwallet.mewwalletbl.core.api.ws.node

import com.google.gson.reflect.TypeToken
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.Parser
import com.myetherwallet.mewwalletbl.core.json.JsonParser
import com.myetherwallet.mewwalletbl.data.JsonRpcResponse

/**
 * Created by BArtWell on 17.02.2022.
 */

private const val TAG = "NodeParser"

class NodeParser : Parser() {

    override fun <OUT> parse(data: String, clazz: Class<OUT>): Either<Failure.WebSocketError, OUT> {
        try {
            val type = TypeToken.getParameterized(JsonRpcResponse::class.java, clazz).type
            val response = JsonParser.fromJson<JsonRpcResponse<OUT>>(data, type)
            return response.result?.let {
                Either.Right(it)
            } ?: Either.Left(Failure.WebSocketError(response.error!!.error, response.error!!.message))
        } catch (e: Exception) {
            MewLog.e(TAG, "Unable parse node WebSocket response", e)
        }
        return Either.Left(Failure.WEB_SOCKET_PARSING)
    }
}
