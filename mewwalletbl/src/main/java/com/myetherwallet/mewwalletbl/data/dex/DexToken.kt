package com.myetherwallet.mewwalletbl.data.dex

import com.google.gson.annotations.SerializedName

data class DexToken(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String?
)