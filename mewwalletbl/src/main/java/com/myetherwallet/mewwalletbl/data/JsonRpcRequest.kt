package com.myetherwallet.mewwalletbl.data

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.addHexPrefix
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.core.extension.toHexStringWithoutStartZeros
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by BArtWell on 13.09.2019.
 */

class JsonRpcRequest<T>(
    @SerializedName("method")
    private val method: String,
    @SerializedName("params")
    private val params: List<T>
) {

    companion object {
        private val nextId = AtomicInteger(0)

        fun createEstimateGasRequest(transaction: Transaction) : JsonRpcRequest<JsonElement> {
            val jsonObject = JsonObject()
            transaction.from?.let { jsonObject.addProperty("from", it.address) }
            jsonObject.addProperty("to", transaction.to!!.address)
            jsonObject.addProperty("value", transaction.value.toHexStringWithoutStartZeros())
            jsonObject.addProperty("data", transaction.data.toHexString().addHexPrefix())
            val params = listOf(jsonObject)
            return JsonRpcRequest(Method.ESTIMATE_GAS.methodName, params)
        }

        fun createApprovalHandlerRequest(from: Address, data: String, to: Address) : JsonRpcRequest<JsonElement> {
            val jsonObject = JsonObject()
            jsonObject.addProperty("from", from.address)
            jsonObject.addProperty("data", data)
            jsonObject.addProperty("to", to.address)
            val params = listOf(jsonObject, JsonPrimitive("latest"))
            return JsonRpcRequest(Method.APPROVAL_HANDLER.methodName, params)
        }
    }

    @SerializedName("jsonrpc")
    val version: String = "2.0"
    @SerializedName("id")
    val id = nextId.getAndIncrement()

    enum class Method(val methodName: String) {
        GET_BALANCE("eth_getBalance"),
        GAS_PRICE("eth_gasPrice"),
        GET_TRANSACTION_COUNT("eth_getTransactionCount"),
        SEND_RAW_TRANSACTION("eth_sendRawTransaction"),
        APPROVAL_HANDLER("eth_call"),
        ESTIMATE_GAS("eth_estimateGas"),
        MAX_PRIORITY_FEE_PER_GAS("eth_maxPriorityFeePerGas"),
        GET_TRANSACTION_BY_HASH("eth_getTransactionByHash"),
        GET_TRANSACTION_RECEIPT("eth_getTransactionReceipt"),
        GET_BLOCK_BY_NUMBER("eth_getBlockByNumber");

        override fun toString() = methodName
    }
}
