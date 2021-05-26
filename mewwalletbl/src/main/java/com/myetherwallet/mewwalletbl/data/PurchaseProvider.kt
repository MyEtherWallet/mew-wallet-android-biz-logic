package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * Created by BArtWell on 12.02.2020.
 */

@Parcelize
data class PurchaseProvider(
    @SerializedName("name")
    val name: Name,
    @SerializedName("fiat_currencies")
    val fiatCurrencies: List<String>,
    @SerializedName("crypto_currencies")
    val cryptoCurrencies: List<String>,
    @SerializedName("prices")
    val prices: List<PurchasePrices>,
    @SerializedName("limits")
    val limits: List<PurchaseLimit>,
    @SerializedName("conversion_rates")
    val rates: List<PurchaseConversionRate>
) : Parcelable {

    fun getLimit(currency: String) = limits.find {
        it.fiatCurrency == currency && it.type == "WEB"
    }!!.limit

    fun getPrice(currency: String) = prices.find { it.fiatCurrency == currency }?.price

    fun getRate(currency: String) = rates.find { it.fiatCurrency == currency }?.exchangeRate

    companion object {

        fun getProvider(response: List<PurchaseProvider>): PurchaseProvider? {
            hasNonZeroLimits(response, Name.SIMPLEX)?.let {
                return it
            }
            hasNonZeroLimits(response, Name.WYRE)?.let {
                return it
            }
            return null
        }

        private fun hasNonZeroLimits(response: List<PurchaseProvider>, search: Name): PurchaseProvider? {
            val provider = response.find { it.name == search }
            provider?.limits?.forEach {
                if (it.type == "WEB" && it.limit.max > BigDecimal.ZERO) {
                    return provider
                }
            }
            return null
        }

        fun isProviderPresent(data: List<PurchaseProvider>?, name: Name): Boolean {
            return data?.find { it.name == name } != null
        }
    }

    enum class Name {
        WYRE, SIMPLEX
    }
}
