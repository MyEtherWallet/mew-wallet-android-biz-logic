package com.myetherwallet.mewwalletbl.data.api.binance

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class BinanceToken(
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("bcSymbol")
    val bcSymbol: String,
    @SerializedName("ethSymbol")
    val ethSymbol: String,
    @SerializedName("bscSymbol")
    val bscSymbol: String,
    @SerializedName("icon")
    val icon: String,
    @SerializedName("minAmount")
    val minAmount: BigDecimal,
    @SerializedName("maxAmount")
    val maxAmount: BigDecimal,
    @SerializedName("promotion")
    val promotion: Boolean,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("bscContractAddress")
    val bscContractAddress: Address,
    @SerializedName("bscContractDecimal")
    val bscContractDecimal: BigDecimal,
    @SerializedName("ethContractAddress")
    val ethContractAddress: Address,
    @SerializedName("ethContractDecimal")
    val ethContractDecimal: BigDecimal,
    @SerializedName("bscGasExchangeEnabled")
    val bscGasExchangeEnabled: Boolean,
    @SerializedName("bscGasExchangeNetworkFee")
    val bscGasExchangeNetworkFee: BigDecimal
) : Parcelable