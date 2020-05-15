package com.myetherwallet.mewwalletbl.core.api.dex

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.dex.DexPrice
import com.myetherwallet.mewwalletbl.data.dex.DexToken
import com.myetherwallet.mewwalletbl.data.dex.DexTrade
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import retrofit2.Call
import retrofit2.HttpException

class DexApiRepository(private val service: DexApi) {

    fun getTokenList(): Either<Failure, List<DexToken>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getFullTokenList()) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPricesFrom(fromSymbol: String, toSymbol: String, fromAmount: String): Either<Failure, List<DexPrice>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPricesFrom(fromSymbol, toSymbol, fromAmount)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPricesTo(fromSymbol: String, toSymbol: String, toAmount: String): Either<Failure, List<DexPrice>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPricesTo(fromSymbol, toSymbol, toAmount)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPriceFrom(fromSymbol: String, toSymbol: String, fromAmount: String, dex: String): Either<Failure, DexPrice> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPriceFrom(fromSymbol, toSymbol, fromAmount, dex)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPriceTo(fromSymbol: String, toSymbol: String, toAmount: String, dex: String): Either<Failure, DexPrice> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPriceTo(fromSymbol, toSymbol, toAmount, dex)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTradeFrom(fromSymbol: String, toSymbol: String, fromAmount: String, dex: String): Either<Failure, DexTrade> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTradeFrom(fromSymbol, toSymbol, fromAmount, dex, BuildConfig.DEX_PROXY)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTradeTo(fromSymbol: String, toSymbol: String, toAmount: String, dex: String): Either<Failure, DexTrade> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTradeTo(fromSymbol, toSymbol, toAmount, dex, BuildConfig.DEX_PROXY)) { it }
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