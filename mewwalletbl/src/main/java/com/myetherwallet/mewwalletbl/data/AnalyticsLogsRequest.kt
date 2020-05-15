package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 20.04.2020.
 */

data class AnalyticsLogsRequest internal constructor(
    @SerializedName("id")
    val id: String,
    @SerializedName("error")
    val log: String
) {

    constructor(event: AnalyticsEvent.Id, log: String) : this(event.value, log)
}
