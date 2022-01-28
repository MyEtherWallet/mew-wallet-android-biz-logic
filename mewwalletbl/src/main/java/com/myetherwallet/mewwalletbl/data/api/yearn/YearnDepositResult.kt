package com.myetherwallet.mewwalletbl.data.api.yearn

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.eip.eip1559.Eip1559Transaction
import java.math.BigDecimal
import java.math.BigInteger

class YearnDepositResult(
    @SerializedName("info")
    val info: Info,
    @SerializedName("transactions")
    val transactions: List<Transaction>
) {

    class Info(
        @SerializedName("type")
        val type: YearnType,
        @SerializedName("amount")
        val amount: BigDecimal,
        @SerializedName("token")
        val token: String
    )

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
    ) {

        fun toTransaction(chainId: BigInteger, nonce: BigInteger, maxFeePerGas: BigInteger, maxPriorityFeePerGas: BigInteger) = Eip1559Transaction(
            nonce,
            maxPriorityFeePerGas,
            maxFeePerGas,
            gas,
            to,
            value,
            data.hexToByteArray(),
            from,
            null,
            null,
            chainId
        )
    }
}