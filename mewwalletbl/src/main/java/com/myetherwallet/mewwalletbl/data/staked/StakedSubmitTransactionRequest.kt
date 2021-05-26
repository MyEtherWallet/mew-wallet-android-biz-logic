package com.myetherwallet.mewwalletbl.data.staked

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 05.12.2020.
 */

data class StakedSubmitTransactionRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("provisioning_request_uuid")
    val uuid: String,
    @SerializedName("rawTx")
    val addHexPrefix: String
)
