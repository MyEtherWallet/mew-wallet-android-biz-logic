package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem

data class MarketOverviewResponse(
    @SerializedName("period")
    val period: String,
    @SerializedName("gainers")
    val gainers: List<MarketItem>,
    @SerializedName("losers")
    val losers: List<MarketItem>,
)