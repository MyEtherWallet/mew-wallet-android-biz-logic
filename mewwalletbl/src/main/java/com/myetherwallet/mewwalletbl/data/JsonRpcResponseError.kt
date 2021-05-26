package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 06.11.2020.
 */

data class JsonRpcResponseError(
    @SerializedName("code")
    var error: Int,
    @SerializedName("message")
    var message: String
)