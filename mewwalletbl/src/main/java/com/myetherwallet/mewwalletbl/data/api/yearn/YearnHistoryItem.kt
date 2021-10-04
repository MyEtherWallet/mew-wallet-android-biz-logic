package com.myetherwallet.mewwalletbl.data.api.yearn

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

class YearnHistoryItem(
    @SerializedName("type")
    val type: YearnType,
    @SerializedName("token")
    val token: String,
    @SerializedName("contract_address")
    val contract: Address,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("amount")
    val amount: BigInteger,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("timestamp")
    val timestamp: Date
)