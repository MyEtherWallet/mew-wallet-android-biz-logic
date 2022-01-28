package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.parcelize.Parcelize

@Parcelize
data class DexPair(
    @SerializedName("from")
    val from: Address,
    @SerializedName("to")
    val to: Address,
    @SerializedName("amount")
    val amount: String
) : Parcelable