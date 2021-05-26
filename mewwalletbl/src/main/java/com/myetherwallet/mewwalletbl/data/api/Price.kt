package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.util.*

data class Price(
    @SerializedName("contract_address")
    val contractAddress: Address,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("icon_png")
    val icon: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("sparkline")
    val sparkline: List<String>
)