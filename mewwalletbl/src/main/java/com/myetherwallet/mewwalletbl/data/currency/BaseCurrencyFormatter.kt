package com.myetherwallet.mewwalletbl.data.currency

import com.myetherwallet.mewwalletbl.data.AppCurrency
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by BArtWell on 09.03.2021.
 */

abstract class BaseCurrencyFormatter(protected val appCurrency: AppCurrency) {

    abstract val isStartsWithSymbol: Boolean

    abstract fun format(amount: BigDecimal, decimals: Int = 2, suffix: AppCurrency.Suffix? = null): String

    protected fun formatWithDecimals(amount: BigDecimal, decimals: Int): String {
        var decimalsFormat = "#".repeat(decimals)
        if (decimalsFormat.isNotEmpty()) {
            decimalsFormat = ".$decimalsFormat"
        }
        val decimalFormat = DecimalFormat("#,###$decimalsFormat")
        return decimalFormat.format(amount)
    }
}
