package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.toTokenValue
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 19.09.2019.
 */

@Parcelize
open class BalanceInfo(
    val id: Long,
    val descriptionId: Long,
    val amount: BigInteger,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val contract: Address,
    val timestamp: Date,
    val address: Address,
    val price: BigDecimal,
    val isHiddenToken: Boolean,
    @ColumnInfo(name = "anonymous_id")
    val anonymousId: String,
    @Ignore
    var currency: AppCurrency = AppCurrency.USD,
    @Ignore
    var exchangeRate: BigDecimal = BigDecimal.ONE
) : Parcelable {

    constructor(ext: ExtBalanceInfo) : this(
        ext.id,
        ext.descriptionId,
        ext.amount,
        ext.decimals,
        ext.name,
        ext.symbol,
        ext.logo,
        ext.contract,
        ext.timestamp,
        ext.address,
        ext.price,
        ext.isHiddenToken,
        ext.anonymousId,
        ext.currency,
        ext.exchangeRate
    )

    fun getBalance() = amount.toTokenValue(decimals)

    fun getIndex() = (id - 1).toInt()
}
