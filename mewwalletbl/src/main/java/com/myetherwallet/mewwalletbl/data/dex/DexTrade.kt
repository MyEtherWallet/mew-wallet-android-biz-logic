package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.dex.trade.DexMetadata
import com.myetherwallet.mewwalletbl.data.dex.trade.DexTradeData
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexTrade(
    @SerializedName("trade")
    val trade: DexTradeData,
    @SerializedName("metadata")
    val metadata: DexMetadata
) : Parcelable