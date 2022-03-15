package com.myetherwallet.mewwalletbl.core.api.node

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.BaseRepository
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.JsonRpcResponseConverter
import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import java.math.BigInteger


/**
 * Created by BArtWell on 13.09.2019.
 */

class NodeApiRepository(private val service: NodeApi) : BaseRepository() {

    suspend fun getBalance(address: Address, period: String): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_BALANCE.methodName, listOf(address.address, period))
        return requestSuspend({ service.getBalance(getNodeUrlPrefix(), createHeaders(), jsonRpc) }) { JsonRpcResponseConverter(it).toWalletBalance() }
    }

    suspend fun getBlockByNumberWithTransactions(block: String, blockchain: Blockchain? = null): Either<Failure, BlockResponseWithTransactions> {
        val jsonRpc = JsonRpcRequest<Any>(JsonRpcRequest.Method.GET_BLOCK_BY_NUMBER.methodName, listOf(block, true))
        return requestSuspend({ service.getBlockByNumberWithTransactions(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc) }) { it.getOrThrow() }
    }

    suspend fun getGasPrice(blockchain: Blockchain? = null): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest<Unit>(JsonRpcRequest.Method.GAS_PRICE.methodName, emptyList())
        return requestSuspend({ service.getGasPrice(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc) }) { JsonRpcResponseConverter(it).toWalletBalance() }
    }

    suspend fun getBlockByNumber(block: String, blockchain: Blockchain? = null): Either<Failure, BlockResponse> {
        val jsonRpc = JsonRpcRequest<Any>(JsonRpcRequest.Method.GET_BLOCK_BY_NUMBER.methodName, listOf(block, false))
        return requestSuspend({ service.getBlockByNumber(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc) }) { it.result!! }
    }

    suspend fun getMaxPriorityFeePerGas(blockchain: Blockchain? = null): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest<Unit>(JsonRpcRequest.Method.MAX_PRIORITY_FEE_PER_GAS.methodName, emptyList())
        return requestSuspend({ service.getMaxPriorityFeePerGas(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc) }) { JsonRpcResponseConverter(it).toBigInteger() }
    }

    suspend fun getTransactionCount(address: Address, period: String, blockchain: Blockchain? = null): Either<Failure, BigInteger> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_COUNT.methodName, listOf(address.address, period))
        return requestSuspend({ service.getNonce(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc) }) { JsonRpcResponseConverter(it).toBigInteger() }
    }

    suspend fun sendRawTransaction(hash: String, blockchain: Blockchain? = null): Either<Failure, String> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.SEND_RAW_TRANSACTION.methodName, listOf(hash))
        return requestSuspend({ service.sendRawTransaction(getNodeUrlPrefix(blockchain), createHeaders(blockchain), jsonRpc) }) { it.getOrThrow() }
    }

    suspend fun getTransactionByHash(hash: String, blockchain: Blockchain? = null): Either<Failure, TransactionResponse> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_BY_HASH.methodName, listOf(hash))
        return requestSuspend({ service.getTransactionByHash(getNodeUrlPrefix(blockchain), createHeaders(), jsonRpc) }) { it.getOrThrow() }
    }

    suspend fun getTransactionReceipt(hash: String, blockchain: Blockchain? = null): Either<Failure, TransactionReceiptResponse> {
        val jsonRpc = JsonRpcRequest(JsonRpcRequest.Method.GET_TRANSACTION_RECEIPT.methodName, listOf(hash))
        return requestSuspend({ service.getTransactionReceipt(getNodeUrlPrefix(blockchain), createHeaders(), jsonRpc) }) { it.getOrThrow() }
    }

    suspend fun getEstimateGas(transaction: Transaction): Either<Failure, BigInteger> {
        return requestSuspend({ service.getEstimateGas(getNodeUrlPrefix(), createHeaders(), JsonRpcRequest.createEstimateGasRequest(transaction)) }) { JsonRpcResponseConverter(it).toWalletBalance() }
    }

    suspend fun getApprovalHandler(from: Address, data: String): Either<Failure, String> {
        return requestSuspend({
            service.getApprovalHandler(
                getNodeUrlPrefix(),
                createHeaders(),
                JsonRpcRequest.createApprovalHandlerRequest(from, data, Address.create(BuildConfig.DEX_PROXY))
            )
        }
        ) { it.getOrThrow() }
    }

    private fun getNodeUrlPrefix(blockchain: Blockchain? = null): String {
        val activeBlockchain = blockchain ?: Preferences.persistent.getBlockchain()
        return activeBlockchain.nodePrefix
    }

    private fun createHeaders(blockchain: Blockchain? = null): Map<String, String> {
        val headers = mutableMapOf<String, String>()
        headers["content-type"] = "application/json"
        val activeBlockchain = blockchain ?: Preferences.persistent.getBlockchain()
        if (activeBlockchain == Blockchain.BSC) {
            headers["source"] = "mew.app.android"
        }
        return headers
    }
}
