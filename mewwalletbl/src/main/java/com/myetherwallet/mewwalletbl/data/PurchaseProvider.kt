package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * Created by BArtWell on 12.02.2020.
 */

@Parcelize
data class PurchaseProvider(
    @SerializedName("name")
    val name: Name?,
    @SerializedName("fiat_currencies")
    val fiatCurrencies: List<String>,
    @SerializedName("crypto_currencies")
    val cryptoCurrencies: List<String>,
    @SerializedName("ach")
    val ach: Boolean,
    @SerializedName("prices")
    val prices: List<PurchasePrices>,
    @SerializedName("limits")
    val limits: List<PurchaseLimit>,
    @SerializedName("conversion_rates")
    val rates: List<PurchaseConversionRate>
) : Parcelable {

    fun getLimit(currency: String) = limits.find {
        (it.fiatCurrency == currency || it.cryptoCurrency == currency) && it.type == "WEB"
    }?.limit ?: PurchaseLimitValues(BigDecimal.ZERO, BigDecimal.ZERO)

    fun getPrice(blockchain: Blockchain, currency: String) = prices.find { it.cryptoCurrency == blockchain.token && it.fiatCurrency == currency }?.price

    fun getPrice(currency: String) = prices.find { it.fiatCurrency == currency }?.price ?: BigDecimal.ZERO

    fun getRate(currency: String) = rates.find { it.fiatCurrency == currency }?.exchangeRate ?: BigDecimal.ONE

    fun getCryptoCurrency() = cryptoCurrencies.firstOrNull() ?: ""

    enum class Name {
        SIMPLEX, MOONPAY
    }
}
