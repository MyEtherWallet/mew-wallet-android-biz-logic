package com.myetherwallet.mewwalletbl.core.api.node

import com.google.gson.JsonElement
import com.myetherwallet.mewwalletbl.data.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by BArtWell on 21.09.2019.
 */

interface NodeApi {

    @POST("{networkId}")
    suspend fun getBalance(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): JsonRpcResponse<String>

    @POST("{networkId}")
    suspend fun getGasPrice(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<Unit>): JsonRpcResponse<String>

    @POST("{networkId}")
    suspend fun getNonce(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): JsonRpcResponse<String>

    @POST("{networkId}")
    suspend fun sendRawTransaction(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): JsonRpcResponse<String>

    @POST("{networkId}")
    suspend fun getTransactionByHash(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): JsonRpcResponse<TransactionResponse>

    @POST("{networkId}")
    suspend fun getTransactionReceipt(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): JsonRpcResponse<TransactionReceiptResponse>

    @POST("{networkId}")
    suspend fun getEstimateGas(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<JsonElement>): JsonRpcResponse<String>

    @POST("{networkId}")
    suspend fun getApprovalHandler(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<JsonElement>): JsonRpcResponse<String>

    @POST("{networkId}")
    suspend fun getBlockByNumberWithTransactions(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<Any>): JsonRpcResponse<BlockResponseWithTransactions>
}
