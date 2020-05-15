package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Created by BArtWell on 12.02.2020.
 */

data class PurchaseSimplexQuote(
    @SerializedName("payment_id")
    val paymentId: String,
    @SerializedName("crypto_amount")
    val cryptoAmount: BigDecimal,
    @SerializedName("crypto_currency")
    val cryptoCurrency: String,
    @SerializedName("fiat_amount")
    val fiatAmount: BigDecimal,
    @SerializedName("fiat_currency")
    val fiatCurrency: String
)
