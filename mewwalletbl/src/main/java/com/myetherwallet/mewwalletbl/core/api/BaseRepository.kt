package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.util.NetworkHandler
import retrofit2.Call
import retrofit2.HttpException

/**
 * Created by BArtWell on 06.10.2021.
 */

open class BaseRepository {

    protected suspend fun <T, R> requestSuspend(call: suspend () -> T, transform: (T) -> R): Either<Failure, R> {
        return if (NetworkHandler.isNetworkConnected()) {
            try {
                val response = call()
                Either.Right(transform(response))
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                Either.Left(Failure.ApiError(throwable))
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
                            Either.Left(Failure.ApiError(IllegalStateException("Body is empty")))
                        } else {
                            Either.Right(transform(body))
                        }
                    }
                    false -> {
                        Either.Left(Failure.ApiError(HttpException(response)))
                    }
                }
            } catch (exception: Throwable) {
                exception.printStackTrace()
                Either.Left(Failure.ApiError(exception))
            }
        } else {
            Either.Left(Failure.NetworkConnection())
        }
    }
}
