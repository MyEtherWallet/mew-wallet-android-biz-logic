package com.myetherwallet.mewwalletbl.data.api.lido

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
class LidoToken(
    @SerializedName("contract_address")
    val contract: Address,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("price")
    val price: BigDecimal,
    @SerializedName("decimals")
    val decimals: Int,
    @SerializedName("icon_png")
    val icon: String,
    @SerializedName("website")
    val website: String
): Parcelable