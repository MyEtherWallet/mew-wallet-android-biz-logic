package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toTokenValue
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger
import java.util.*

@Parcelize
class ExtBalanceInfo(
    val amount: BigInteger,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val contract: Address,
    val timestamp: Date,
    val address: Address,
    val price: Double,
    val accountName: String,
    val hide: Boolean,
    var tokensCount: Int?,
    val position: Long
) : Parcelable {

    fun getBalance() = amount.toTokenValue(decimals)
}
