package com.myetherwallet.mewwalletbl.core.api.node

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.JsonRpcResponseConverter
import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import retrofit2.Call
import retrofit2.HttpException
import java.math.BigInteger


/**
 * Created by BArtWell on 13.09.2019.
 */

class NodeApiRepository(private val service: NodeApi) {

    fun getBalance(address: Address, period: String): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_BALANCE.methodName, listOf(address.address, period))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getBalance(getNodeUrlPrefix(), createHeaders(), jsonRpc)) { JsonRpcResponseConverter(it).toWalletBalance() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getBlockByNumberWithTransactions(block: String, blockchain: Blockchain? = null): Either<Failure, BlockResponseWithTransactions> {
        val jsonRpc = JsonRpcRequest<Any>(JsonRpcRequest.Method.GET_BLOCK_BY_NUMBER.methodName, listOf(block, true))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getBlockByNumberWithTransactions(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc)) { it.result!! }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getGasPrice(blockchain: Blockchain? = null): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest<Unit>(JsonRpcRequest.Method.GAS_PRICE.methodName, emptyList())
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getGasPrice(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc)) { JsonRpcResponseConverter(it).toWalletBalance() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransactionCount(address: Address, period: String, blockchain: Blockchain? = null): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_COUNT.methodName, listOf(address.address, period))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getNonce(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc)) { JsonRpcResponseConverter(it).toBigInteger() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun sendRawTransaction(hash: String, blockchain: Blockchain? = null): Either<Failure, String> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.SEND_RAW_TRANSACTION.methodName, listOf(hash))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.sendRawTransaction(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc)) { it.result!! }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransactionByHash(hash: String, blockchain: Blockchain? = null): Either<Failure, TransactionResponse> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_BY_HASH.methodName, listOf(hash))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTransactionByHash(getNodeUrlPrefix(blockchain), createHeaders(), jsonRpc)) { it.result!! }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransactionReceipt(hash: String, blockchain: Blockchain? = null): Either<Failure, TransactionReceiptResponse> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_RECEIPT.methodName, listOf(hash))
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTransactionReceipt(getNodeUrlPrefix(blockchain), createHeaders(), jsonRpc)) { it.result!! }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getEstimateGas(transaction: Transaction): Either<Failure, BigInteger> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getEstimateGas(getNodeUrlPrefix(), createHeaders(), JsonRpcRequest.createEstimateGasRequest(transaction))) { JsonRpcResponseConverter(it).toWalletBalance() }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getApprovalHandler(from: Address, data: String): Either<Failure, String> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getApprovalHandler(getNodeUrlPrefix(), createHeaders(), JsonRpcRequest.createApprovalHandlerRequest(from, data, Address.create(BuildConfig.DEX_PROXY)))) { it.result!! }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    private fun <Q, T : JsonRpcResponse<Q>, R> request(call: Call<T>, transform: (T: JsonRpcResponse<Q>) -> R): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> {
                    val body = response.body()
                    if (body?.result == null) {
                        if (body?.error == null) {
                            Either.Left(Failure.ServerError(IllegalStateException("Body is empty")))
                        } else {
                            Either.Left(Failure.ServerError(IllegalStateException(body.error!!.message)))
                        }
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

    private fun getNodeUrlPrefix(blockchain: Blockchain? = null): String {
        val activeBlockchain = blockchain ?: Preferences.persistent.getBlockchain()
        return activeBlockchain.nodePrefix
    }

    private fun createHeaders(blockchain: Blockchain? = null): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["content-type"] = "application/json"
        val activeBlockchain = blockchain ?: Preferences.persistent.getBlockchain()
        if(activeBlockchain == Blockchain.BSC) {
            headers["source"] = "mew.app.android"
        }
        return headers
    }
}
