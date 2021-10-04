package com.myetherwallet.mewwalletbl.data

import com.myetherwallet.mewwalletkit.core.extension.formatCurrency
import com.myetherwallet.mewwalletkit.core.extension.enumValueOfOrNull
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

enum class AppCurrency {

    USD, EUR, RUB, JPY, AUD, CAD, GBP;

    private val currency = Currency.getInstance(name)
    private val format by lazy { createDecimalFormat() }
    val isStartsWithSymbol: Boolean by lazy { format.positiveSuffix.isNullOrEmpty() }
    val symbol = currency.symbol ?: name

    fun format(amount: BigDecimal, decimals: Int = 2, suffix: Suffix? = null): String {
        format.maximumFractionDigits = decimals

        var formatted = if(!isStartsWithSymbol && suffix != null) {
            createDecimalFormat(true).format(amount).trim()
        } else {
            format.format(amount)
        }
        if (isStartsWithSymbol && symbol.length > 1) {
            formatted = formatted.replace(symbol, "$symbol ")
        }
        return if (suffix == null) {
            formatted
        } else {
            if (isStartsWithSymbol) {
                "$formatted$suffix"
            } else {
                "$formatted$suffix $symbol"
            }
        }
    }

    private fun createDecimalFormat(cutSymbol: Boolean = false): DecimalFormat {
        val decimalFormat = NumberFormat.getCurrencyInstance() as DecimalFormat
        decimalFormat.currency = currency
        if (cutSymbol) {
            val symbols = decimalFormat.decimalFormatSymbols
            symbols.currencySymbol = ""
            decimalFormat.decimalFormatSymbols = symbols
        }
        return decimalFormat
    }

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
                isStartsWithSymbol = appCurrency.isStartsWithSymbol
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
