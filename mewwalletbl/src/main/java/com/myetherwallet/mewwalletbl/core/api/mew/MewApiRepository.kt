package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import retrofit2.Call
import retrofit2.HttpException

/**
 * Created by BArtWell on 21.09.2019.
 */

class MewApiRepository(private val service: MewApi) {

    fun getAccountTokens(address: Address): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getAccountTokens(address.address)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTokens(contractAddresses: List<String>? = null): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTokens(contractAddresses)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getMetaTokens(): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getMetaTokens()) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPrices(request: RequestByContracts): Either<Failure, List<Price>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPrices(request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getBalances(address: Address, limit: Int, request: RequestByContracts): Either<Failure, List<TokenBalance>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getBalances(address.address, limit, request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransactionHistory(address: String, limit: Int, offset: Int, request: RequestByContracts): Either<Failure, List<Transaction>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTransactionHistory(address, limit, offset, request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransaction(hash: String): Either<Failure, Transaction> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTransaction(hash)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun sendFcmToken(token: String, addresses: List<String>): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.sendFcmToken(SendFcmTokenRequest(token, addresses))) { it }
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
