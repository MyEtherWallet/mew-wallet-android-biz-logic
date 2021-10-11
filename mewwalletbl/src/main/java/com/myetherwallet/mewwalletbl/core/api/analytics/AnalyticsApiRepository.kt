package com.myetherwallet.mewwalletbl.core.api.analytics

import com.myetherwallet.mewwalletbl.core.api.BaseRepository
import com.myetherwallet.mewwalletbl.data.AnalyticsEvent
import com.myetherwallet.mewwalletbl.data.AnalyticsEventsRequest
import com.myetherwallet.mewwalletbl.data.AnalyticsLogsRequest

/**
 * Created by BArtWell on 24.02.2020.
 */

class AnalyticsApiRepository(private val service: AnalyticsApi) : BaseRepository() {

    suspend fun submit(iso: String, events: List<AnalyticsEvent>) = requestSuspend({ service.submit("android", iso, AnalyticsEventsRequest(events)) }) { it }

    suspend fun uploadLogs(logsRequest: AnalyticsLogsRequest) = requestSuspend({ service.uploadLogs(logsRequest) }) { it }
}
