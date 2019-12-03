package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 24.07.2019.
 */

data class EncryptedMessage(
    @SerializedName("ciphertext")
    val ciphertext: MessageByteArray,
    @SerializedName("ephemPublicKey")
    val ephemPublicKey: MessageByteArray,
    @SerializedName("iv")
    val iv: MessageByteArray,
    @SerializedName("mac")
    val mac: MessageByteArray
) : BaseMessage() {

    constructor(ciphertext: ByteArray, ephemPublicKey: ByteArray, iv: ByteArray, mac: ByteArray) : this(
        MessageByteArray(ciphertext),
        MessageByteArray(ephemPublicKey),
        MessageByteArray(iv),
        MessageByteArray(mac)
    )
}
