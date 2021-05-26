package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import java.math.BigInteger
import java.util.*

data class Transaction(
    @SerializedName("hash")
    val hash: String,
    @SerializedName("address")
    val address: String?,
    @SerializedName("contract_address")
    val contractAddress: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("balance")
    val balance: String,
    @SerializedName("delta")
    val delta: String?,
    @SerializedName("value")
    val value: String?,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("block_number")
    val blockNumber: Int,
    @SerializedName("status")
    val status: TransactionStatus?,
    @SerializedName("timestamp")
    val timestamp: Date?,
    @SerializedName("nonce")
    val nonce: BigInteger?
) {

    fun getTransactionStatusOrPending() = status ?: TransactionStatus.PENDING

    fun getTimestampOrCurrent() = timestamp ?: Date()
}
