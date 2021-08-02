package com.myetherwallet.mewwalletbl.data.currency

import com.myetherwallet.mewwalletbl.data.AppCurrency
import java.math.BigDecimal
import java.text.DecimalFormat

/**
 * Created by BArtWell on 09.03.2021.
 */

class EndCurrencyFormatter(appCurrency: AppCurrency) : BaseCurrencyFormatter(appCurrency) {

    override val isStartsWithSymbol = false

    override fun format(amount: BigDecimal, decimals: Int, suffix: AppCurrency.Suffix?): String {
        val formatted = formatWithDecimals(amount, decimals)
        return if (suffix == null) {
            "$formatted\u2009" + appCurrency.symbol
        } else {
            "$formatted$suffix " + appCurrency.symbol
        }
    }
}
