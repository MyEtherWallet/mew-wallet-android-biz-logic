package com.myetherwallet.mewwalletbl.data.api.market

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.IgnoredOnParcel
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
    @SerializedName("price")
    val price: BigDecimal?,
    @SerializedName("timestamp")
    val timestamp: Date?,
    @SerializedName("sparkline")
    val sparkline: List<String>?
    ) : Parcelable {

    @IgnoredOnParcel
    @SerializedName("entry_title")
    var entryTitle: String? = null

    @IgnoredOnParcel
    @SerializedName("description")
    var description: Localization? = null

    @IgnoredOnParcel
    @SerializedName("type")
    var interval: String? = null

    @IgnoredOnParcel
    @SerializedName("price_change_percentage")
    var priceChangePercentage: String? = null

    @IgnoredOnParcel
    @SerializedName("ath")
    var ath: String? = null

    @IgnoredOnParcel
    @SerializedName("rank")
    var rank: Int? = null

    @IgnoredOnParcel
    @SerializedName("tags")
    var tags: List<Localization>? = emptyList()

    @IgnoredOnParcel
    @SerializedName("market_cap")
    var marketCap: BigDecimal? = null

    @IgnoredOnParcel
    @SerializedName("volume_24h")
    var volume24h: BigDecimal? = null

    @IgnoredOnParcel
    @SerializedName("total_supply")
    var totalSupply: BigDecimal? = null

    @IgnoredOnParcel
    @SerializedName("circulating_supply")
    var circulatingSupply: BigDecimal? = null

    @SerializedName("currency")
    var currency: AppCurrency = AppCurrency.USD
        get() = field

    @SerializedName("exchange_rate")
    var exchangeRate = BigDecimal.ONE
        get() = if (field == BigDecimal.ZERO) BigDecimal.ONE else field

    fun setCurrencyAndRate(value: Pair<AppCurrency, BigDecimal>) {
        currency = value.first
        exchangeRate = value.second
    }
}
