package com.myetherwallet.mewwalletbl.data.staked

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import kotlinx.android.parcel.Parcelize
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

