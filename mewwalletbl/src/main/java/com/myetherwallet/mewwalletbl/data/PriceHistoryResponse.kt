package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

data class PriceHistoryResponse(
    @SerializedName("prices")
    val prices: List<List<String>>,
    @SerializedName("volumes")
    val volumes: List<List<String>>
)