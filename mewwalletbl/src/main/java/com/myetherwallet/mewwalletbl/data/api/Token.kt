package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("contract_address")
    val address: String,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("email")
    val email: String
)