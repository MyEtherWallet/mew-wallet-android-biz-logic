package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 09.09.2021.
 */

data class BlockResponseTransaction(
    @SerializedName("from")
    val from: String,
    @SerializedName("nonce")
    val nonce: String
)
