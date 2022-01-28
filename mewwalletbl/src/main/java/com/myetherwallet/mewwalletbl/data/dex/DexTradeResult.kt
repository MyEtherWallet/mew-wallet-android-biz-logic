package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class DexTradeResult(
    @SerializedName("pair")
    val pair: DexPair,
    @SerializedName("quote")
    val quote: DexQuote,
    @SerializedName("transactions")
    val transactions: List<DexTrade>
) : Parcelable