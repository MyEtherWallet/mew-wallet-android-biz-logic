package com.myetherwallet.mewwalletbl.data.api.swap

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.BigInteger

@Parcelize
data class SwapTrade(
    @SerializedName("provider")
    val provider: String,
    @SerializedName("from_amount")
    val fromAmount: BigDecimal,
    @SerializedName("to_amount")
    val toAmount: BigDecimal,
    @SerializedName("gas")
    val gas: BigInteger,
    @SerializedName("price_impact")
    val priceImpact: BigDecimal,
    @SerializedName("max_slippage")
    val maxSlippage: BigDecimal,
    @SerializedName("minimum")
    val minimum: BigDecimal,
    @SerializedName("fee")
    val fee: BigDecimal,
    @SerializedName("transfer_fee")
    val transferFee: Boolean,
    @SerializedName("transactions")
    val transactions: List<Transaction>
) : Parcelable {

    @Parcelize
    class Transaction(
        @SerializedName("from")
        val from: Address,
        @SerializedName("to")
        val to: Address,
        @SerializedName("data")
        val data: String,
        @SerializedName("value")
        val value: BigInteger,
        @SerializedName("gas")
        val gas: BigInteger
    ) : Parcelable
}