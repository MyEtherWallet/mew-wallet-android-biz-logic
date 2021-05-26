package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

/**
 * Created by BArtWell on 30.12.2020.
 */
@Parcelize
data class TransactionResponse(
    @SerializedName("blockHash")
    val blockHash: String?,
    @SerializedName("blockNumber")
    val blockNumber: String?,
    @SerializedName("from")
    val from: String,
    @SerializedName("gas")
    val gas: String,
    @SerializedName("gasPrice")
    val gasPrice: String,
    @SerializedName("hash")
    val hash: String,
    @SerializedName("input")
    val input: String,
    @SerializedName("nonce")
    val nonce: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("transactionIndex")
    val transactionIndex: String,
    @SerializedName("value")
    val value: String,
    @SerializedName("v")
    val v: String,
    @SerializedName("r")
    val r: String,
    @SerializedName("s")
    val s: String,
    @Ignore
    var currency: AppCurrency = AppCurrency.USD,
    @Ignore
    var exchangeRate: BigDecimal = BigDecimal.ONE
) : Parcelable
