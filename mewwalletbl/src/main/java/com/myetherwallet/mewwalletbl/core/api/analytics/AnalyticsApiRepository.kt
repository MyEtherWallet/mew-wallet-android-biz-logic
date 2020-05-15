package com.myetherwallet.mewwalletbl.core.api.analytics

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.AnalyticsEvent
import com.myetherwallet.mewwalletbl.data.AnalyticsEventsRequest
import com.myetherwallet.mewwalletbl.data.AnalyticsLogsRequest
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import retrofit2.Call
import retrofit2.HttpException

/**
 * Created by BArtWell on 24.02.2020.
 */

class AnalyticsApiRepository(private val service: AnalyticsApi) {

    fun submit(iso: String, events: List<AnalyticsEvent>): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.submit("android", iso, AnalyticsEventsRequest(events))) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun uploadLogs(logsRequest: AnalyticsLogsRequest): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.uploadLogs(logsRequest)) { it }
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
