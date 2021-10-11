package com.myetherwallet.mewwalletbl.data.api.lido

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.math.BigInteger

@Parcelize
class LidoInfo(
    @SerializedName("current_apr")
    val apr: BigDecimal,
    @SerializedName("total_staked")
    val totalStaked: BigInteger,
    @SerializedName("staking_fee")
    val stakingFee: BigDecimal,
    @SerializedName("token")
    val token: LidoToken
) : Parcelable
