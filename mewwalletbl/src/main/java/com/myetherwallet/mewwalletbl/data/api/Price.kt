package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.util.*

data class Price(
    @SerializedName("contract_address")
    val contractAddress: Address,
    @SerializedName("price")
    val price: String,
    @SerializedName("timestamp")
    val timestamp: Date
)