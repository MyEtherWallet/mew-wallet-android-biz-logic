package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class SwapTokenDescription(
    val id: Long,
    val blockchain: Blockchain,
    @ColumnInfo(name = "contract_address")
    val contractAddress: Address,
    val name: String,
    val symbol: String,
    val icon: String?,
    val decimals: Int,
    val timestamp: Date,
    val price: BigDecimal,
    @ColumnInfo(name = "volume_24h")
    val volume24h: BigDecimal,
    val category: SwapTokenCategory
) : Parcelable {
    @Ignore
    var currency: AppCurrency = AppCurrency.USD

    @Ignore
    var exchangeRate: BigDecimal = BigDecimal.ONE

}