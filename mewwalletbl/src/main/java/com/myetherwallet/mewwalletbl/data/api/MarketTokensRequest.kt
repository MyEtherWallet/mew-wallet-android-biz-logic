package com.myetherwallet.mewwalletbl.data.api


import com.google.gson.annotations.SerializedName

data class MarketTokensRequest(
    @SerializedName("contractAddresses")
    val contracts: List<String>
)
