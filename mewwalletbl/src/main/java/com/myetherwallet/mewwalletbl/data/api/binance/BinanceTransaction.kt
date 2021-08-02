package com.myetherwallet.mewwalletbl.data.api.binance

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
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
    fun toTransaction(nonce: BigInteger, gasPrice: BigInteger, chainId: BigInteger, contractAddress: Address? = null) = Transaction(
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
}