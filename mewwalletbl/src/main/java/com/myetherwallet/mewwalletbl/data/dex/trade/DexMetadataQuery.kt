package com.myetherwallet.mewwalletbl.data.dex.trade

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexMetadataQuery(
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("fromAmount")
    val fromAmount: String,
    @SerializedName("toAmount")
    val toAmount: String?,
    @SerializedName("dex")
    val dex: String,
    @SerializedName("proxy")
    val proxy: String
) : Parcelable