package com.myetherwallet.mewwalletbl.data.dex.trade

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DexMetadataInput(
    @SerializedName("address")
    val address: String,
    @SerializedName("amount")
    val amount: String
) : Parcelable