package com.myetherwallet.mewwalletbl.data.dex.trade

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexTradeData(
    @SerializedName("to")
    val to: String,
    @SerializedName("data")
    val data: String,
    @SerializedName("value")
    val value: String
) : Parcelable