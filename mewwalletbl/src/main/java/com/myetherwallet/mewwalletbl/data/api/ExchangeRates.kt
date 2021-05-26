package com.myetherwallet.mewwalletbl.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

@Parcelize
data class ExchangeRates(

        @SerializedName("fiat_currency")
        val fiat: String,

        @SerializedName("exchange_rate")
        val exchangeRate: BigDecimal

) : Parcelable