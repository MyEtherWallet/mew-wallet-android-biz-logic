package com.myetherwallet.mewwalletbl.data.api.binance

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class BinanceQuota(
    @SerializedName("total")
    val total: BigDecimal,
    @SerializedName("used")
    val used: BigDecimal,
    @SerializedName("left")
    val left: BigDecimal,
)
