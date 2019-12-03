package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 25.07.2019.
 */

data class WebRtcMessage<T>(
    val type: Type,
    val data: T
) : BaseMessage() {

    enum class Type {
        @SerializedName("address")
        ADDRESS,
        @SerializedName("signTx")
        SIGN_TX,
        @SerializedName("signMessage")
        SIGN_MESSAGE
    }
}
