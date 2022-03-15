package com.myetherwallet.mewwalletbl.data.api.swap

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem

class SwapListResult(
    @SerializedName("featured")
    val featured: List<MarketItem>,
    @SerializedName("tokens")
    val tokens: List<MarketItem>
)