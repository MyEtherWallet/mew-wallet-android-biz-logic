package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

data class TokenBalance(
    @SerializedName("contract_address")
    val contractAddress: Address,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("timestamp")
    val timestamp: Date,
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("website")
    val website: String,
    @SerializedName("sparkline")
    val sparkline: List<String>,
    @SerializedName("amount")
    val amount: BigInteger,
    @SerializedName("locked_amount")
    val lockedAmount: BigInteger?
) {

    fun getContract() = if (contractAddress.address == Address.DEFAULT_API_CONTRACT) {
        Address.createDefault()
    } else {
        contractAddress
    }
}
