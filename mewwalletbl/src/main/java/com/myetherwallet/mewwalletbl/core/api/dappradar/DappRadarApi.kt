package com.myetherwallet.mewwalletbl.core.api.dappradar

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.data.dappradar.DappRadarItem
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DappRadarApi {

    @GET("dapps")
    @Headers("Authorization: ${BuildConfig.DAPP_RADAR_TOKEN}", "Content-type: application/json")
    suspend fun getDapps(@Query("page") page: Int, @Query("itemsPerPage") itemsPerPage: Int): List<DappRadarItem>
}
