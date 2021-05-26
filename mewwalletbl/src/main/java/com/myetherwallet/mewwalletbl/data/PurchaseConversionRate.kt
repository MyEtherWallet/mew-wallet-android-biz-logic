package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * Created by BArtWell on 02.04.2021.
 */

@Parcelize
data class PurchaseConversionRate(
    @SerializedName("fiat_currency")
    val fiatCurrency: String,
    @SerializedName("exchange_rate")
    val exchangeRate: BigDecimal
) : Parcelable
