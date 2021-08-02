package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PurchaseLimit(
        @SerializedName("type")
        val type: String,
        @SerializedName("fiat_currency")
        val fiatCurrency: String,
        @SerializedName("limit")
        val limit: PurchaseLimitValues
) : Parcelable
