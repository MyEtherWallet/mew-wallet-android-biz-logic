package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class DexQuote(
    @SerializedName("dex")
    val dex: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("amount")
    val amount: BigDecimal,
    @Ignore
    @SerializedName("rank")
    val rank: Int? = null
) : Parcelable