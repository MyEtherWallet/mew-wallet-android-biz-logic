package com.myetherwallet.mewwalletbl.data.dex

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.util.*

data class DexToken(
    @SerializedName("contract_address")
    val contract_address: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("icon_png")
    val icon: String,
    @SerializedName("price")
    val price: BigDecimal?,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("volume_24h")
    val volume_24h: String
) {

    fun getPriceOrZero() = price ?: BigDecimal.ZERO
}
