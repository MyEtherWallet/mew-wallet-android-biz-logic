package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.util.NetworkHandler
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by BArtWell on 06.10.2021.
 */

open class BaseRepository() {

    protected suspend fun <T, R> requestSuspend(call: suspend () -> T, transform: (T) -> R): Either<Failure, R> {
        return if (NetworkHandler.isNetworkConnected()) {
            try {
                val response = call()
                Either.Right(transform(response))
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                when (throwable) {
                    is IOException -> Either.Left(Failure.ServerError(IllegalStateException("Network error")))
                    is HttpException -> {
                        Either.Left(Failure.CommonError(throwable.code(), throwable.message()))
                    }
                }
                Either.Left(Failure.UnknownError(throwable))
            }
        } else {
            Either.Left(Failure.NetworkConnection())
        }
    }

    protected fun <T, R> request(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return if (NetworkHandler.isNetworkConnected()) {
            try {
                val response = call.execute()
                when (response.isSuccessful) {
                    true -> {
                        val body = response.body()
                        if (body == null) {
                            Either.Left(Failure.ServerError(IllegalStateException("Body is empty")))
                        } else {
                            Either.Right(transform(body))
                        }
                    }
                    false -> {
                        val bodyError = response.errorBody()
                        if (bodyError == null) {
                            Either.Left(Failure.ServerError(HttpException(response)))
                        } else {
                            Either.Left(Failure.CommonError(response.code(), bodyError.string()))
                        }
                    }
                }
            } catch (exception: Throwable) {
                exception.printStackTrace()
                Either.Left(Failure.UnknownError(exception))
            }
        } else {
            Either.Left(Failure.NetworkConnection())
        }
    }
}
