package com.myetherwallet.mewwalletbl.core.api.ws.data

import com.google.gson.reflect.TypeToken
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.json.JsonParser

/**
 * Created by BArtWell on 02.09.2021.
 */

private const val TAG = "Chain"

internal class Chain<IN, OUT : Any>(val request: Request<IN>, private val clazz: Class<OUT>, val callback: (Either<Failure.WebSocketError, OUT>) -> Unit) {

    fun setResult(data: String?) {
        val result: Either<Failure.WebSocketError, OUT> = data?.let { parse(it) } ?: Either.Left(Failure.WEB_SOCKET_EMPTY)
        callback(result)
    }

    private fun parse(data: String): Either<Failure.WebSocketError, OUT> {
        try {
            val type = TypeToken.getParameterized(Response::class.java, this.clazz).type
            val response: Response<OUT> = JsonParser.fromJson(data, type)
            return Either.Right(response.response.body)
        } catch (ignored: Exception) {
            try {
                val type = object : TypeToken<Response<ErrorBody>>() {}.type
                val response: Response<ErrorBody> = JsonParser.fromJson(data, type)
                return Either.Left(Failure.WebSocketError(response.response.statusCode, response.response.body.error))
            } catch (e: Exception) {
                MewLog.e(TAG, "Unable parse WebSocket response", e)
            }
        }
        return Either.Left(Failure.WEB_SOCKET_PARSING)
    }
}
