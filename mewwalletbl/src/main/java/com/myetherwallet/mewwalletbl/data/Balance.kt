package com.myetherwallet.mewwalletbl.data

import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by BArtWell on 13.09.2019.
 */

data class Balance(
    val balance: BigInteger,
    val decimals: Int,
    val symbol: String,
    val address: String,
    val name: String?,
    val website: String?,
    val email: String?
) {

    fun calculateBalance(): BigDecimal {
        return BigDecimal(balance).divide(BigDecimal.TEN.pow(decimals))
    }
}
