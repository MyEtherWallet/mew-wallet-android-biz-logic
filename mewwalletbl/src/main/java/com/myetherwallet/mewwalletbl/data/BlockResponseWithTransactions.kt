package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.extension.toDate
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import java.util.*

/**
 * Created by BArtWell on 04.08.2021.
 */

data class BlockResponseWithTransactions(
    @SerializedName("timestamp")
    val timestamp: String,
    @SerializedName("transactions")
    val transactions: List<BlockResponseTransaction>
) {

    fun getTimestampDate(): Date {
        val seconds = timestamp.hexToBigInteger().toLong()
        val milliseconds = seconds * 1000L
        return milliseconds.toDate()
    }
}
