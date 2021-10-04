package com.myetherwallet.mewwalletbl.data.api.yearn

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toTokenValue
import java.math.BigDecimal
import java.math.BigInteger

class YearnBalance(
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
    @SerializedName("profit")
    val profit: BigInteger,
    @SerializedName("decimals")
    val decimals: Int
) {
    fun getAmount() = amount.toTokenValue(decimals)

    fun getProfit() = profit.toTokenValue(decimals)

}