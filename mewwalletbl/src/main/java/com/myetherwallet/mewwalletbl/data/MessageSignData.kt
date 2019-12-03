package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 25.07.2019.
 */

data class MessageSignData(
    @SerializedName("address")
    private val address: String,
    @SerializedName("sig")
    private val signature: String
)
