package com.myetherwallet.mewwalletbl.core.api.node

import com.google.gson.JsonElement
import com.myetherwallet.mewwalletbl.data.JsonEstimatedGasResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface EstimatedGasApi {

    @POST("{networkId}")
    @Headers("content-type: application/json")
    fun getMultipleEstimateGas(@Path("networkId") networkId: String, @Body jsonRpc: EstimatedGasApiRepository.JsonRpcRequest<JsonElement>): Call<JsonEstimatedGasResponse>
}