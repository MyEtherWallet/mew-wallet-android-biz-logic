package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 25.02.2020.
 */

data class AnalyticsEventsRequest(
    @SerializedName("events")
    val events: List<AnalyticsEvent>
)
