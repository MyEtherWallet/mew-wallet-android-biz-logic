package com.myetherwallet.mewwalletbl.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.core.extension.*
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
) : Parcelable {

    companion object {

        fun calculateHash(text: String): ByteArray {
            return if (text.isHex(true)) {
                text.removeHexPrefix().hexToByteArray().hashPersonalMessage()
            } else {
                text.hashPersonalMessage()
            }
        }
    }
}
