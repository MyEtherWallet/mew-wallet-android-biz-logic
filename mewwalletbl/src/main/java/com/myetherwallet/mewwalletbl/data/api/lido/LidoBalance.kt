package com.myetherwallet.mewwalletbl.data.api.lido

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toTokenValue
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

class LidoBalance(
    @SerializedName("contract_address")
    val contract: Address,
    @SerializedName("amount")
    val amount: BigInteger,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("timestamp")
    val timestamp: Date
) {
    fun getAmount() = amount.toTokenValue(decimals)
}