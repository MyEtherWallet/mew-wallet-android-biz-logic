package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class DexPrice(
    val fromContract: Address,
    val toContract: Address,
    val exchange: String,
    val dex: String,
    val price: BigDecimal,
    val marketImpact: String? = null,
    val scale: Int? = null
) : Parcelable {

    var availability = Status.AVAILABLE

    fun getPair(): Pair<Address, Address> {
        return Pair(this.fromContract, this.toContract)
    }

    enum class Status {
        AVAILABLE, PENDING, UNAVAILABLE
    }
}