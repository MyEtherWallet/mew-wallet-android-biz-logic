package com.myetherwallet.mewwalletbl.data.staked

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

/**
 * Created by BArtWell on 26.11.2020.
 */

data class StakedTransactions(
    @SerializedName("value")
    private val value: BigInteger,
    @SerializedName("data")
    private val data: ByteArray,
    @SerializedName("gas")
    private val gas: BigInteger
)
