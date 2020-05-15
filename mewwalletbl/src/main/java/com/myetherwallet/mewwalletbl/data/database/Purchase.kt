package com.myetherwallet.mewwalletbl.data.database

import com.myetherwallet.mewwalletbl.data.PurchaseState
import java.math.BigDecimal
import java.util.*

data class Purchase(
    val transactionId: String,
    val fiatAmount: BigDecimal,
    val fiatCurrency: String,
    val cryptoAmount: BigDecimal,
    val cryptoSymbol: String,
    val cryptoLogo: String,
    val timestamp: Date,
    val status: PurchaseState,
    val provider: String
)