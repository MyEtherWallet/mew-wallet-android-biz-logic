package com.myetherwallet.mewwalletbl.data.api.binance

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.eip.eip155.LegacyTransaction
import com.myetherwallet.mewwalletkit.eip.eip1559.Eip1559Transaction
import java.math.BigInteger

data class BinanceTransaction(
    @SerializedName("to")
    val to: Address,
    @SerializedName("from")
    val from: Address,
    @SerializedName("data")
    val data: String,
    @SerializedName("value")
    val value: BigInteger,
    @SerializedName("gas")
    val gas: BigInteger
) {
    fun toTransaction(nonce: BigInteger, chainId: BigInteger, gasPrice: BigInteger, contractAddress: Address? = null) = LegacyTransaction(
        nonce,
        gasPrice,
        gas,
        contractAddress ?: to,
        value,
        data.hexToByteArray(),
        from,
        null,
        chainId
    )

    fun toTransaction(nonce: BigInteger, chainId: BigInteger, maxFeePerGas: BigInteger, maxPriorityFeePerGas: BigInteger, contractAddress: Address? = null) = Eip1559Transaction(
        nonce,
        maxPriorityFeePerGas,
        maxFeePerGas,
        gas,
        contractAddress ?: to,
        value,
        data.hexToByteArray(),
        from,
        null,
        null,
        chainId
    )
}