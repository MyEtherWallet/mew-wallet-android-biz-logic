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

    fun getPrice(blockchain: Blockchain, currency: String) = prices.find { it.cryptoCurrency == blockchain.token && it.fiatCurrency == currency }?.price

    fun getRate(currency: String) = rates.find { it.fiatCurrency == currency }?.exchangeRate

    companion object {

        fun getProvider(response: List<PurchaseProvider>, blockchain: Blockchain): PurchaseProvider? {
            hasNonZeroLimits(response, Name.SIMPLEX, blockchain)?.let {
                return it
            }
            return null
        }

        private fun hasNonZeroLimits(response: List<PurchaseProvider>, search: Name, blockchain: Blockchain): PurchaseProvider? {
            val provider = response.find { it.name == search }
            val isProviderSupportToken = provider?.cryptoCurrencies?.find { it == blockchain.token } != null
            if (isProviderSupportToken) {
                provider?.limits?.forEach {
                    if (it.type == "WEB" && it.limit.max > BigDecimal.ZERO) {
                        return provider
                    }
                }
            }
            return null
        }

        fun isProviderPresent(data: List<PurchaseProvider>?, name: Name): Boolean {
            return data?.find { it.name == name } != null
        }
    }

    enum class Name {
        SIMPLEX
    }
}
