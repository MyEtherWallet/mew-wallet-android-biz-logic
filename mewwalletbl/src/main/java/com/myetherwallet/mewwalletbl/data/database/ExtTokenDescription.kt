package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import androidx.room.Ignore
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class ExtTokenDescription(
    val id: Long,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val contract: Address,
    val price: BigDecimal?,
    val volume_24h: Double
) : Parcelable {
    @Ignore
    var currency: AppCurrency = AppCurrency.USD

    @Ignore
    var exchangeRate: BigDecimal = BigDecimal.ONE
}