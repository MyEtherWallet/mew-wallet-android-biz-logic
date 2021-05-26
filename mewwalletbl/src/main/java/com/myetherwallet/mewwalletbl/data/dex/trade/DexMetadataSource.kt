package com.myetherwallet.mewwalletbl.data.dex.trade

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class DexMetadataSource(
    @SerializedName("dex")
    val dex: String,
    @SerializedName("price")
    val price: BigDecimal
) : Parcelable