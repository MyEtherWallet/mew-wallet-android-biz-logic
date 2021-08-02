package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 25.07.2019.
 */

data class WebRtcMessage<T>(
    val type: Type?,
    val data: T
) : BaseMessage() {

    enum class Type {
        @SerializedName("address")
        ADDRESS,
        @SerializedName("signTx")
        SIGN_TX,
        @SerializedName("signMessage")
        SIGN_MESSAGE,
        @SerializedName("reject")
        REJECT,
        @SerializedName("eth_getEncryptionPublicKey")
        GET_ENCRYPTION_PUBLIC_KEY,
        @SerializedName("eth_decrypt")
        DECRYPT,
        @SerializedName("eth_signTypedData_v3")
        SIGN_TYPED_DATA_V3
    }
}
