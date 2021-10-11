package com.myetherwallet.mewwalletbl.core.api.analytics

import com.myetherwallet.mewwalletbl.data.AnalyticsEventsRequest
import com.myetherwallet.mewwalletbl.data.AnalyticsLogsRequest
import retrofit2.http.*

/**
 * Created by BArtWell on 13.09.2019.
 */

interface AnalyticsApi {

    @POST("analytics/record/{platform}")
    @Headers("content-type: application/json")
    suspend fun submit(@Path("platform") platform: String, @Query("iso") iso: String, @Body events: AnalyticsEventsRequest): Any

    @POST("/analytics/logs")
    @Headers("content-type: application/json")
    suspend fun uploadLogs(@Body logs: AnalyticsLogsRequest): Any
}
