package com.myetherwallet.mewwalletbl.data

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.core.json.JsonParser

/**
 * Created by BArtWell on 01.08.2019.
 */

data class SignalMessage(
    @SerializedName("signal")
    val signal: String?,
    @SerializedName("data")
    val data: JsonElement,
    @SerializedName("message")
    val rawMessage: String
) {
    val message by lazy { JsonParser.fromJson(rawMessage, SocketMessage::class.java) }
}