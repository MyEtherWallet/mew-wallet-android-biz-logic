package com.myetherwallet.mewwalletbl.data

import com.myetherwallet.mewwalletbl.data.currency.BaseCurrencyFormatter
import com.myetherwallet.mewwalletbl.data.currency.DefaultCurrencyFormatter
import com.myetherwallet.mewwalletbl.data.currency.EndCurrencyFormatter
import com.myetherwallet.mewwalletbl.data.currency.StartCurrencyFormatter
import com.myetherwallet.mewwalletkit.core.extension.formatCurrency
import com.myetherwallet.mewwalletkit.core.extension.enumValueOfOrNull
import java.math.BigDecimal

enum class AppCurrency(val symbol: String, private val formatterClass: Class<out BaseCurrencyFormatter> = DefaultCurrencyFormatter::class.java) {

    USD("$", StartCurrencyFormatter::class.java),
    EUR("€"),
    RUB("₽", EndCurrencyFormatter::class.java);

    val formatter: BaseCurrencyFormatter by lazy {
        formatterClass
            .getConstructor(AppCurrency::class.java)
            .newInstance(this)
    }

    fun format(amount: BigDecimal, decimals: Int = 2, suffix: Suffix? = null) = formatter.format(amount, decimals, suffix)

    companion object {

        fun format(amount: BigDecimal, currency: String, decimals: Int = 2, suffix: Suffix? = null) =
            format(amount, currency, decimals, suffix) { _, _, _ ->
                amount.formatCurrency(currency, decimals) {
                    if (suffix == null) {
                        "$amount $currency"
                    } else {
                        "$amount $suffix $currency"
                    }
                }
            }

        fun format(amount: BigDecimal, currency: String, decimals: Int = 2, suffix: Suffix? = null, onError: (amount: BigDecimal, suffix: Suffix?, currency: String) -> String): String {
            val appCurrency = enumValueOfOrNull<AppCurrency>(currency)
            return appCurrency?.format(amount, decimals, suffix) ?: onError(amount, suffix, currency)
        }

        fun getSymbolAndPosition(currency: String): Pair<String, Boolean> {
            val isStartsWithSymbol: Boolean
            val symbol: String
            val appCurrency = enumValueOfOrNull<AppCurrency>(currency)
            if (appCurrency == null) {
                isStartsWithSymbol = false
                symbol = currency
            } else {
                isStartsWithSymbol = appCurrency.formatter.isStartsWithSymbol
                symbol = appCurrency.symbol
            }
            return Pair(symbol, isStartsWithSymbol)
        }

        fun getDefault() = Pair<AppCurrency, BigDecimal>(USD, BigDecimal.ONE)
    }

    data class Suffix(
        val suffix: String,
        val withSpace: Boolean = true
    ) {

        override fun toString() = if (withSpace) {
            " $suffix"
        } else {
            suffix
        }
    }
}
