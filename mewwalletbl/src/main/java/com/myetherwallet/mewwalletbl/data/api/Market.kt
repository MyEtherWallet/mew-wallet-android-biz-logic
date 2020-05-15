package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

data class Market(
    @SerializedName("results")
    val results: List<MarketItem>,
    @SerializedName("paginationToken")
    val paginationToken: String
)