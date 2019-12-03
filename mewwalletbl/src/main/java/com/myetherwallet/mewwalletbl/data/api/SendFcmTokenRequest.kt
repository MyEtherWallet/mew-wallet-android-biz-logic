package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 20.11.2019.
 */

data class SendFcmTokenRequest(
    @SerializedName("token")
    val token: String,
    @SerializedName("addresses")
    val addresses: List<String>
)
