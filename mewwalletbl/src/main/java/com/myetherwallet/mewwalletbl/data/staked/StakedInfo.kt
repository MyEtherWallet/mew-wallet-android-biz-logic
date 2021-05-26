package com.myetherwallet.mewwalletbl.data.staked

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 26.11.2020.
 */

@Parcelize
data class StakedInfo(

    @SerializedName("apr")
    val apr: String,

    @SerializedName("total_staked")
    val totalStaked: BigInteger,

    @SerializedName("mew_fee_percent")
    val mewFeePercent: String,

    @SerializedName("mew_fee")
    val mewFee: String,

    @SerializedName("estimated_activation_timestamp")
    val estimatedActivationTimestamp: Date

) : Parcelable
