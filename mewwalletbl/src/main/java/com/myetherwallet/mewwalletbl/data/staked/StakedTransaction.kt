package com.myetherwallet.mewwalletbl.data.staked

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.eip.eip1559.Eip1559Transaction
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

/**
 * Created by BArtWell on 05.12.2020.
 */

@Parcelize
data class StakedTransaction(
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
) : Parcelable {

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

