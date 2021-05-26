package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.myetherwallet.mewwalletbl.data.database.TokenDescription
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class DexPrice(
    val fromDescription: TokenDescription,
    val toDescription: TokenDescription,
    val exchange: String,
    val dex: String,
    val price: BigDecimal,
    val marketImpact: String? = null,
    val scale: Int? = null
) : Parcelable {

    var availability = Status.AVAILABLE

    fun getPair(): Pair<Address, Address> {
        return Pair(this.fromDescription.contract, this.toDescription.contract)
    }

    enum class Status {
        AVAILABLE, PENDING, UNAVAILABLE
    }
}