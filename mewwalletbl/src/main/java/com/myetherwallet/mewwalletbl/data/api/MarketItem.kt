package com.myetherwallet.mewwalletbl.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.database.EntityMarket
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class MarketItem(
    @SerializedName("contract_address")
    val contractAddress: Address,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("symbol")
    val symbol: String = "",
    @SerializedName("decimals")
    val decimals: Int = 18,
    @SerializedName("icon_png")
    val icon: String = "",
    @SerializedName("website")
    val website: String = "",
    @SerializedName("price")
    val price: String,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("market_cap")
    val marketCap: String? = null,
    @SerializedName("volume_24h")
    val volume24h: String? = null,
    @SerializedName("total_supply")
    val totalSupply: String? = null,
    @SerializedName("circulating_supply")
    val circulatingSupply: String? = null
) : Parcelable {
    constructor(entity: EntityMarket) : this(
        entity.contract, entity.name, entity.symbol, entity.decimals, entity.logo, entity.website, entity.price, entity.timestamp
    )

    fun getPrice() = BigDecimal.valueOf(price.toDoubleOrNull() ?: 0.0)
}