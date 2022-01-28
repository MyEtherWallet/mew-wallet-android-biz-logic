package com.myetherwallet.mewwalletbl.data.api.yearn

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
class YearnInfo(
    @SerializedName("token")
    val token: String,
    @SerializedName("contract_address")
    val contract: Address,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("apy")
    val apy: BigDecimal,
    @SerializedName("fees")
    val fees: BigDecimal,
    @SerializedName("tvl")
    val tvl: BigDecimal
) : Parcelable