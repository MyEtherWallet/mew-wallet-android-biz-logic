package com.myetherwallet.mewwalletbl.core.api.dappradar

import com.myetherwallet.mewwalletbl.data.dappradar.DappRadarItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DappRadarApi {
    @GET("dapps")
    @Headers("Authorization: ", "Content-type: application/json")
    fun getDapps(@Query("page") page: Int, @Query("itemsPerPage") itemsPerPage: Int): Call<List<DappRadarItem>>
}