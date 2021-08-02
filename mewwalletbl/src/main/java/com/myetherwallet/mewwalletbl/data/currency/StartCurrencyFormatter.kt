package com.myetherwallet.mewwalletbl.data.currency

import com.myetherwallet.mewwalletbl.data.AppCurrency
import java.math.BigDecimal

/**
 * Created by BArtWell on 09.03.2021.
 */

class StartCurrencyFormatter(appCurrency: AppCurrency) : BaseCurrencyFormatter(appCurrency) {

    override val isStartsWithSymbol = true

    override fun format(amount: BigDecimal, decimals: Int, suffix: AppCurrency.Suffix?): String {
        var result = appCurrency.symbol + formatWithDecimals(amount, decimals)
        suffix?.let {
            result = "$result$suffix"
        }
        return result
    }
}
