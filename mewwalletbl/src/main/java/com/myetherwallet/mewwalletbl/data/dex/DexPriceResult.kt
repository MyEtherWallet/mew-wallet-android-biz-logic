package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexPriceResult (
    @SerializedName("pair")
    val pair: DexPair,
    @SerializedName("quotes")
    val quotes: List<DexQuote>
) : Parcelable