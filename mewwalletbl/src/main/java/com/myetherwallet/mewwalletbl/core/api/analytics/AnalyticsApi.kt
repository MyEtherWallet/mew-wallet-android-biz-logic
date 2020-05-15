package com.myetherwallet.mewwalletbl.core.api.analytics

import com.myetherwallet.mewwalletbl.data.*
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by BArtWell on 13.09.2019.
 */

interface AnalyticsApi {

    @POST("analytics/record/{platform}")
    @Headers("content-type: application/json")
    fun submit(@Path("platform") platform: String, @Query("iso") iso: String, @Body events: AnalyticsEventsRequest): Call<Any>

    @POST("/analytics/logs")
    @Headers("content-type: application/json")
    fun uploadLogs(@Body logs: AnalyticsLogsRequest): Call<Any>
}
