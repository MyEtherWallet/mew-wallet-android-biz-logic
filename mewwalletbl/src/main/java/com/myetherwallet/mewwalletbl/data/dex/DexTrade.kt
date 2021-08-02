package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexTrade(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("data")
    val data: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("gas")
    val gas: String
) : Parcelable