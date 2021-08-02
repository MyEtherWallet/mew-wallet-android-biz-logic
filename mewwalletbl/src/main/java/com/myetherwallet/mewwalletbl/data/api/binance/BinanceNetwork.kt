package com.myetherwallet.mewwalletbl.data.api.binance

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class BinanceNetwork(
    @SerializedName("name")
    val name: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("swapFeeRate")
    val swapFeeRate: BigDecimal,
    @SerializedName("networkFee")
    val networkFee: BigDecimal,
    @SerializedName("supportLabel")
    val supportLabel: Boolean,
    @SerializedName("labelName")
    val labelName: String?,
    @SerializedName("labelRegex")
    val labelRegex: String?,
    @SerializedName("txUrl")
    val txUrl: String?,
    @SerializedName("depositEnabled")
    val depositEnabled: Boolean,
    @SerializedName("withdrawEnabled")
    val withdrawEnabled: Boolean,
    @SerializedName("withdrawAmountUnit")
    val withdrawAmountUnit: BigDecimal?,
    @SerializedName("withdrawMinAmount")
    val withdrawMinAmount: BigDecimal?,
    @SerializedName("withdrawMaxAmount")
    val withdrawMaxAmount: BigDecimal?,
    @SerializedName("addressRegex")
    val addressRegex: String?,
    @SerializedName("tokenStandard")
    val tokenStandard: String,
    @SerializedName("requiredConfirms")
    val requiredConfirms: Int,
    @SerializedName("tokenPerBNB")
    val tokenPerBNB: BigDecimal,
    @SerializedName("minimumTokenForGasExchange")
    val minimumTokenForGasExchange: BigDecimal
) : Parcelable
