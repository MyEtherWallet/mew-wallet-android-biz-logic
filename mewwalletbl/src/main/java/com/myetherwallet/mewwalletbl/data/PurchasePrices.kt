package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class PurchasePrices(
        @SerializedName("fiat_currency")
        val fiatCurrency: String,
        @SerializedName("crypto_currency")
        val cryptoCurrency: String,
        @SerializedName("price")
        val price: BigDecimal
) : Parcelable
