package com.myetherwallet.mewwalletbl.data.dappradar

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DappRadarItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("deepLink")
    val deepLink: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("protocol")
    val protocol: String,
    @SerializedName("category")
    val category: String
) : Parcelable