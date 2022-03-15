package com.myetherwallet.mewwalletbl.data.api.swap

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class SwapRate(
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("rate")
    val rate: BigDecimal
) : Parcelable