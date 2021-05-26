package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import java.util.*

class EstimateTransactionSpeed (
    @SerializedName("gas_price")
    val gasPrice: String,
    @SerializedName("estimated_seconds")
    val estimatedSeconds: Int,
    @SerializedName("estimated_timestamp")
    val estimatedTimestamp : Date
)