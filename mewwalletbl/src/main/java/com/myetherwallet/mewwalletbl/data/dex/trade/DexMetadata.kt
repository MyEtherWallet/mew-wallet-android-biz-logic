package com.myetherwallet.mewwalletbl.data.dex.trade

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexMetadata(
    @SerializedName("gasPrice")
    val gasPrice: String?,
    @SerializedName("input")
    val input: DexMetadataInput?,
    @SerializedName("output")
    val output: DexMetadataOutput?,
    @SerializedName("source")
    val source: DexMetadataSource?,
    @SerializedName("query")
    val query: DexMetadataQuery
) : Parcelable