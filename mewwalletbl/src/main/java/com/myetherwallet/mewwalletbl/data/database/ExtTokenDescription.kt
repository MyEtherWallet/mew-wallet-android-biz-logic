package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExtTokenDescription(
    val id: Long,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val contract: Address,
    val price: Double?
) : Parcelable