package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toTokenValue
import kotlinx.android.parcel.Parcelize
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 19.09.2019.
 */

@Parcelize
open class BalanceInfo(
    val id: Long,
    val amount: BigInteger,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val contract: Address,
    val timestamp: Date,
    val address: Address,
    val price: Double,
    @ColumnInfo(name = "anonymous_id")
    val anonymousId: String
) : Parcelable {

    constructor(ext: ExtBalanceInfo) : this(
        ext.id,
        ext.amount,
        ext.decimals,
        ext.name,
        ext.symbol,
        ext.logo,
        ext.contract,
        ext.timestamp,
        ext.address,
        ext.price,
        ext.anonymousId
    )

    fun getBalance() = amount.toTokenValue(decimals)

    fun getIndex() = (id - 1).toInt()
}
