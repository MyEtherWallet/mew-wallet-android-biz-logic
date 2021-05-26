package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem

data class MarketResponse(
    @SerializedName("results")
    val results: List<MarketItem>,
    @SerializedName("paginationToken")
    val paginationToken: String?
)