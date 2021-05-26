package com.myetherwallet.mewwalletbl.data.database

import java.math.BigDecimal

data class TotalBalance(
    val amount: Long,
    val decimals: Int,
    val price: BigDecimal
)