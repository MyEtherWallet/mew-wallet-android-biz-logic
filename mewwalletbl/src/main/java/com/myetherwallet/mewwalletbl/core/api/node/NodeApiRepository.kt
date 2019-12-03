package com.myetherwallet.mewwalletbl.core.api.node

import com.myetherwallet.mewwalletbl.NetworkConfig
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.JsonRpcResponseConverter
import com.myetherwallet.mewwalletbl.data.JsonRpcRequest
import com.myetherwallet.mewwalletbl.data.JsonRpcResponse
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import retrofit2.Call
import retrofit2.HttpException
import java.math.BigInteger

/**
 * Created by BArtWell on 13.09.2019.
 */

private val API_METHOD_ETH = NetworkConfig.current.node

class NodeApiRepository(private val service: NodeApi) {

    fun getBalance(address: Address, period: String): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_BALANCE.methodName, listOf(address.address, period))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getBalance(API_METHOD_ETH, jsonRpc)) { JsonRpcResponseConverter(it).toWalletBalance() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getGasPrice(): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest<Unit>(JsonRpcRequest.Method.GAS_PRICE.methodName, emptyList())
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getGasPrice(API_METHOD_ETH, jsonRpc)) { JsonRpcResponseConverter(it).toWalletBalance() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getNonce(address: Address, period: String): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_COUNT.methodName, listOf(address.address, period))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getNonce(API_METHOD_ETH, jsonRpc)) { JsonRpcResponseConverter(it).toBigInteger() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun sendRawTransaction(hash: String): Either<Failure, String> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.SEND_RAW_TRANSACTION.methodName, listOf(hash))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.sendRawTransaction(API_METHOD_ETH, jsonRpc)) { it.result!! }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    private fun <T : JsonRpcResponse, R> request(call: Call<T>, transform: (T: JsonRpcResponse) -> R): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> {
                    val body = response.body()
                    if (body?.result == null) {
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
