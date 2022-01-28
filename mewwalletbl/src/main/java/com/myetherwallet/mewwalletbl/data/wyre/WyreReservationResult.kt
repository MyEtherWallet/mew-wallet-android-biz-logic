package com.myetherwallet.mewwalletbl.data.wyre

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WyreReservationResult(
    @SerializedName("url")
    val url: String,
    @SerializedName("reservation")
    val reservation: String
) : Parcelable