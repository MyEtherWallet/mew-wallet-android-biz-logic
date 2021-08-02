package com.myetherwallet.mewwalletbl.data.staked

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 02.12.2020.
 */

data class StakedProvisionRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("withdrawalKey")
    val eth2Address: String,
    @SerializedName("validatorsCount")
    val validatorsCount: Int
)
