package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.extension.getMessage
import retrofit2.HttpException

/**
 * Created by BArtWell on 13.09.2019.
 */

sealed class Failure(val throwable: Throwable?) {
    class NetworkConnection : Failure(null)

    class ApiError(throwable: Throwable) : Failure(throwable)
    class DbError(message: String) : Failure(IllegalStateException(message))
    class AlgorithmError(message: String) : Failure(IllegalStateException(message))
    class CodeMessageError(code: Int, message: String) : Failure(CodeMessageThrowable(code, message))

    override fun toString() = throwable?.getMessage() ?: super.toString()

    fun toHttpException(): HttpException? {
        if (throwable is HttpException) {
            return throwable
        }
        return null
    }

    class WebSocketError(val code: Int, val error: String?) : Failure(CodeMessageThrowable(code, error ?: ""))

    class CodeMessageThrowable(val errorCode: Int, val errorMessage: String) : Throwable("Error $errorCode ($errorMessage)")

    companion object {

        val WEB_SOCKET_NOT_CONNECTED = WebSocketError(-1, "Not connected")
        val WEB_SOCKET_EMPTY = WebSocketError(-2, "Empty response")
        val WEB_SOCKET_PARSING = WebSocketError(-3, "Unable to parse data")
    }
}
