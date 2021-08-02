package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.util.*

data class SimplePrice(
    @SerializedName("contract_address")
    val contractAddress: Address,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("timestamp")
    val timestamp: Date
)