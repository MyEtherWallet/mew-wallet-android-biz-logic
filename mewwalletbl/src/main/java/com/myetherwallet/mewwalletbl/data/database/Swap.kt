package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Swap(
    val id: Long,
    val txHash: String?,
    val createTime: Date,
    val fromTokenSymbol: String,
    val fromTokenLogo: String,
    val toTokenSymbol: String,
    val toTokenLogo: String,
    val fromAmount: Double,
    val toAmount: Double,
    val dex: String,
    val accountName: String,
    val accountAddress: Address,
    val toFiatPrice: Double,
    val swapStatus: TransactionStatus,
    val updateTime: Date
) : Parcelable