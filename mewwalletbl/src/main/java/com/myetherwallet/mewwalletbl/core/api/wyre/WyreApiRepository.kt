package com.myetherwallet.mewwalletbl.core.api.wyre

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.wyre.WyreRequestParams
import com.myetherwallet.mewwalletbl.data.wyre.WyreReservationResult
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import retrofit2.Call
import retrofit2.HttpException

private const val TAG = "WyreApiRepository"

class WyreApiRepository(private val service: WyreApi) {

    fun getReservation(wyreRequestParams: WyreRequestParams): Either<Failure, WyreReservationResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getReservation(wyreRequestParams)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    private fun <T, R> request(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return try {
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
                false -> Either.Left(Failure.ServerError(HttpException(response)))
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.UnknownError(exception))
        }
    }
}
