package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by BArtWell on 24.07.2019.
 */

@Parcelize
data class MessageToSign(
    @SerializedName("hash")
    val hash: String,
    @SerializedName("text")
    val text: String
) : Parcelable
