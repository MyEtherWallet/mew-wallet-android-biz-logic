package com.myetherwallet.mewwalletbl.core.api.node

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.JsonEstimatedGasResponse
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.core.extension.addHexPrefix
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.core.extension.toHexStringWithoutStartZeros
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import retrofit2.Call
import retrofit2.HttpException
import java.math.BigInteger
import java.util.concurrent.atomic.AtomicInteger

private val API_METHOD_ETH = "eth"

class EstimatedGasApiRepository(private val service: EstimatedGasApi) {

    fun getMultipleEstimateGas(transaction: Transaction, approvalTransaction: Transaction?): Either<Failure, List<BigInteger>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getMultipleEstimateGas(API_METHOD_ETH, JsonRpcRequest.createMultipleEstimateGasRequest(transaction, approvalTransaction))) {
                it.result!!.map { item -> item.hexToBigInteger() }
            }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    class JsonRpcRequest<T>(
        @SerializedName("method")
        private val method: String,
        @SerializedName("params")
        private val params: List<List<T>>
    ) {

        companion object {
            private val nextId = AtomicInteger(0)

            fun createMultipleEstimateGasRequest(transaction: Transaction, approval: Transaction?): JsonRpcRequest<JsonElement> {
                val params = if (approval == null) {
                    listOf(
                        (listOf(
                            createJsonObject(transaction)
                        ))
                    )
                } else {
                    listOf(
                        (listOf(
                            createJsonObject(approval),
                            createJsonObject(transaction)
                        ))
                    )
                }
                return JsonRpcRequest(Method.ESTIMATE_GAS.methodName, params)
            }

            private fun createJsonObject(transaction: Transaction): JsonObject {
                val jsonObject = JsonObject()
                jsonObject.addProperty("from", transaction.from!!.address)
                jsonObject.addProperty("to", transaction.to!!.address)
                jsonObject.addProperty("value", transaction.value.toHexStringWithoutStartZeros())
                jsonObject.addProperty("data", transaction.data.toHexString().addHexPrefix())
                return jsonObject
            }
        }

        @SerializedName("jsonrpc")
        val version: String = "2.0"

        @SerializedName("id")
        val id = nextId.getAndIncrement()

        enum class Method(val methodName: String) {
            ESTIMATE_GAS("eth_estimateGasList");

            override fun toString() = methodName
        }
    }

    private fun <T : JsonEstimatedGasResponse, R> request(call: Call<T>, transform: (T: JsonEstimatedGasResponse) -> R): Either<Failure, R> {
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