package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

class TransactionReceiptResponse (
    @SerializedName("transactionHash")
    val transactionHash: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("from")
    val from: String,
    @SerializedName("gasUsed")
    val gasUsed: String,
    @SerializedName("contractAddress")
    val contractAddress: String,
    @SerializedName("status")
    val status: String
)