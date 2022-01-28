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

@Parcelize
class ExtBalanceInfo(
    val id: Long,
    val descriptionId: Long,
    var amount: BigInteger,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val contract: Address,
    val timestamp: Date,
    val address: Address,
    @ColumnInfo(name = "eth2_address")
        val eth2Address: String,
    val price: BigDecimal,
    val sparkline: String?,
    val accountName: String,
    var hide: Boolean,
    var tokensCount: Int?,
    var totalFiat: Double?,
    val position: Long,
    val isHiddenToken: Boolean,
    @ColumnInfo(name = "anonymous_id")
        val anonymousId: String,
    @Ignore
        var currency: AppCurrency = AppCurrency.USD,
    @Ignore
        var exchangeRate: BigDecimal = BigDecimal.ONE
) : Parcelable {

    constructor(id: Long,
                descriptionId: Long,
                amount: BigInteger,
                decimals: Int,
                name: String,
                symbol: String,
                logo: String?,
                contract: Address,
                timestamp: Date,
                address: Address,
                eth2Address: String,
                price: BigDecimal,
                sparkline: String?,
                accountName: String,
                hide: Boolean,
                tokensCount: Int?,
                totalFiat: Double?,
                position: Long,
                isHiddenToken: Boolean,
                anonymousId: String) : this(id, descriptionId, amount, decimals, name, symbol, logo, contract, timestamp, address, eth2Address, price, sparkline, accountName, hide, tokensCount, totalFiat, position, isHiddenToken, anonymousId, AppCurrency.USD, BigDecimal.ONE)

    fun getBalance() = amount.toTokenValue(decimals)

    fun getIndex() = (id - 1).toInt()
}
