package com.myetherwallet.mewwalletbl.data.staked

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 05.12.2020.
 */

data class StakedSubmitTransactionResponse(
    @SerializedName("address")
    val address: Address,
    @SerializedName("provisioning_request_uuid")
    val uuid: String,
    @SerializedName("status")
    val status: StakedStatus,
    @SerializedName("hash")
    val hash: String
)
