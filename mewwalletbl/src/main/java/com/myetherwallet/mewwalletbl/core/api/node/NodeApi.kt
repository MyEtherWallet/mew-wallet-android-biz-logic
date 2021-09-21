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
    fun getBalance(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): Call<JsonRpcResponse<String>>

    @POST("{networkId}")
    fun getGasPrice(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<Unit>): Call<JsonRpcResponse<String>>

    @POST("{networkId}")
    fun getNonce(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): Call<JsonRpcResponse<String>>

    @POST("{networkId}")
    fun sendRawTransaction(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): Call<JsonRpcResponse<String>>

    @POST("{networkId}")
    fun getTransactionByHash(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): Call<JsonRpcResponse<TransactionResponse>>

    @POST("{networkId}")
    fun getTransactionReceipt(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<String>): Call<JsonRpcResponse<TransactionReceiptResponse>>

    @POST("{networkId}")
    fun getEstimateGas(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<JsonElement>): Call<JsonRpcResponse<String>>

    @POST("{networkId}")
    fun getApprovalHandler(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<JsonElement>): Call<JsonRpcResponse<String>>

    @POST("{networkId}")
    fun getBlockByNumberWithTransactions(@Path("networkId") networkId: String, @HeaderMap headers: Map<String, String>, @Body jsonRpc: JsonRpcRequest<Any>): Call<JsonRpcResponse<BlockResponseWithTransactions>>
}
