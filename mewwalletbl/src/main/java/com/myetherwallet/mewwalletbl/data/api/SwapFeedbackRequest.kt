package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

data class SwapFeedbackRequest (
    @SerializedName("reason")
    val reason: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("iso")
    val iso: String,
    @SerializedName("device")
    val device: String
)