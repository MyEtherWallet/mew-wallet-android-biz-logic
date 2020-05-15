package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

data class PurchaseHistoryItem(
    @SerializedName("transaction_id")
    val id: String,
    @SerializedName("fiat_amount")
    val fiatAmount: BigDecimal,
    @SerializedName("fiat_currency")
    val fiatCurrency: String,
    @SerializedName("crypto_amount")
    val cryptoAmount: BigInteger,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("status")
    val status: PurchaseState,
    @SerializedName("provider")
    val provider: String
)
