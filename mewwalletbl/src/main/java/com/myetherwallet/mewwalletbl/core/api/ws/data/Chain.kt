package com.myetherwallet.mewwalletbl.core.api.ws.data

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.Parser

/**
 * Created by BArtWell on 02.09.2021.
 */

internal class Chain<OUT : Any>(val request: BaseRequest, val parser: Parser, private val clazz: Class<OUT>, val callback: (Either<Failure.WebSocketError, OUT>) -> Unit) {

    fun setResult(data: String?) {
        val result: Either<Failure.WebSocketError, OUT> = data?.let {
            parser.parse(it, clazz)
        } ?: Either.Left(Failure.WEB_SOCKET_EMPTY)
        callback(result)
    }
}
