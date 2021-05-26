package com.myetherwallet.mewwalletbl.core.api.dappradar

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.dappradar.DappRadarItem
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import retrofit2.Call
import retrofit2.HttpException

private const val TAG = "DappRadarRepository"

class DappRadarRepository(private val service: DappRadarApi) {

    fun getDapps(page: Int, itemsPerPage: Int): Either<Failure, List<DappRadarItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getDapps(page, itemsPerPage)) { it }
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