package com.myetherwallet.mewwalletbl.data.currency

import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletkit.core.extension.formatCurrency
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.NumberFormat

/**
 * Created by BArtWell on 09.03.2021.
 */

class DefaultCurrencyFormatter(appCurrency: AppCurrency) : BaseCurrencyFormatter(appCurrency) {

    private val format = NumberFormat.getCurrencyInstance() as DecimalFormat
    override val isStartsWithSymbol: Boolean by lazy { format.positiveSuffix.isNullOrEmpty() }

    override fun format(amount: BigDecimal, decimals: Int, suffix: AppCurrency.Suffix?): String {
        return if (suffix == null) {
            amount.formatCurrency(format, appCurrency.name, decimals) {
                EndCurrencyFormatter(appCurrency).format(amount, decimals, suffix)
            }
        } else {
            val formatter = if (isStartsWithSymbol) {
                StartCurrencyFormatter(appCurrency)
            } else {
                EndCurrencyFormatter(appCurrency)
            }
            formatter.format(amount, decimals, suffix)
        }
    }
}
