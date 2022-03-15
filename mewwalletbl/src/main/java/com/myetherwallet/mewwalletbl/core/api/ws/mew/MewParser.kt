package com.myetherwallet.mewwalletbl.core.api.ws.mew

import com.google.gson.reflect.TypeToken
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.Parser
import com.myetherwallet.mewwalletbl.core.api.ws.data.ResponseErrorContent
import com.myetherwallet.mewwalletbl.core.api.ws.mew.data.MewResponse
import com.myetherwallet.mewwalletbl.core.api.ws.mew.data.MewResponseErrorContent
import com.myetherwallet.mewwalletbl.core.json.JsonParser

/**
 * Created by BArtWell on 15.02.2022.
 */

private const val TAG = "MewParser"

class MewParser : Parser() {

    override fun <OUT> parse(data: String, clazz: Class<OUT>): Either<Failure.WebSocketError, OUT> {
        try {
            val type = TypeToken.getParameterized(MewResponse::class.java, clazz).type
            val response: MewResponse<OUT> = JsonParser.fromJson(data, type)
            return Either.Right(response.response.body)
        } catch (ignored: Exception) {
            try {
                val type = object : TypeToken<MewResponse<ResponseErrorContent>>() {}.type
                val response: MewResponse<MewResponseErrorContent> = JsonParser.fromJson(data, type)
                return Either.Left(Failure.WebSocketError(response.response.statusCode, response.response.body.error))
            } catch (e: Exception) {
                MewLog.e(TAG, "Unable parse WebSocket response", e)
            }
        }
        return Either.Left(Failure.WEB_SOCKET_PARSING)
    }
}
