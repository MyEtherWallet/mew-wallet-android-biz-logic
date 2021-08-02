package com.myetherwallet.mewwalletbl.data.api.binance

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class BinanceStatus(
    @SerializedName("address")
    val address: Address,
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    var status: MoveStatus,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("amount")
    val amount: BigDecimal,
    @SerializedName("fromNetwork")
    val fromNetwork: String,
    @SerializedName("toNetwork")
    val toNetwork: String,
    @SerializedName("fromAddress")
    val fromAddress: Address,
    @SerializedName("toAddress")
    val toAddress: Address,
    @SerializedName("depositAddress")
    val depositAddress: Address,
    @SerializedName("ethContractAddress")
    val ethContractAddress: Address,
    @SerializedName("bscContractAddress")
    val bscContractAddress: Address,
    @SerializedName("ethContractDecimal")
    val ethContractDecimal: Int,
    @SerializedName("bscContractDecimal")
    val bscContractDecimal: Int,
    @SerializedName("swapFee")
    val swapFee: BigDecimal,
    @SerializedName("swapFeeRate")
    val swapFeeRate: BigDecimal,
    @SerializedName("networkFee")
    val networkFee: BigDecimal,
    @SerializedName("depositTimeout")
    val depositTimeout: Date,
    @SerializedName("depositRequiredConfirms")
    val depositRequiredConfirms: String,
    @SerializedName("createTime")
    val createTime: Date,
    @SerializedName("updatedAt")
    val updatedAt: Date?,
    @SerializedName("depositAmount")
    var depositAmount: BigDecimal,
    @SerializedName("swapAmount")
    var swapAmount: BigDecimal,
    @SerializedName("depositReceivedConfirms")
    val depositReceivedConfirms: String,
    @SerializedName("depositHash")
    var depositHash: String,
    @SerializedName("swapHash")
    var swapHash: String,
    @SerializedName("exchangeGasAmount")
    val exchangeGasAmount: BigDecimal,
    @SerializedName("tokenPerBNB")
    val tokenPerBNB: BigDecimal,
    @SerializedName("fiatPrice")
    val fiatPrice: BigDecimal? = null

//    @SerializedName("toAddressLabel")
//    val toAddressLabel: String,
//    @SerializedName("networkFeePromoted")
//    val networkFeePromoted: Boolean,
//    @SerializedName("depositAddressLabel")
//    val depositAddressLabel: String,
//    @SerializedName("depositAddressLabelName")
//    val depositAddressLabelName: Boolean
) : Parcelable {
    @Ignore
    var currency: AppCurrency = AppCurrency.USD

    @Ignore
    var exchangeRate: BigDecimal = BigDecimal.ONE

    fun isPending() = status == MoveStatus.WITHDRAWINPROGRESS || status == MoveStatus.DEPOSITINPROGRESS || status == MoveStatus.WAITINGFORDEPOSIT

    fun isFailed() = status == MoveStatus.FAILED || status == MoveStatus.CANCELLED
}
