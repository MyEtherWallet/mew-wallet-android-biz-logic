package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 04.08.2021.
 */

data class BlockResponse(
    @SerializedName("baseFeePerGas")
    val baseFeePerGas: String
)
