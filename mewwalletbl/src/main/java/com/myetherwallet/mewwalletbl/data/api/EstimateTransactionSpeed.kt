package com.myetherwallet.mewwalletbl.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class EstimateTransactionSpeed (
    @SerializedName("gas_price")
    val gasPrice: String,
    @SerializedName("estimated_seconds")
    val estimatedSeconds: Int,
    @SerializedName("estimated_timestamp")
    val estimatedTimestamp : Date
) : Parcelable