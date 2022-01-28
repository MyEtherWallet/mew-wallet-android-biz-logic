package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

/**
 * Created by BArtWell on 18.03.2021.
 */

@Parcelize
data class PurchaseLimitValues(
    @SerializedName("min")
    val min: BigDecimal,
    @SerializedName("max")
    val max: BigDecimal
) : Parcelable
