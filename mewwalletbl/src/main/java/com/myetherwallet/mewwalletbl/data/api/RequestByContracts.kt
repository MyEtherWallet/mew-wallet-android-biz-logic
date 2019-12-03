package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 08.10.2019.
 */

class RequestByContracts {

    @SerializedName("contractAddresses")
    val contracts = mutableListOf<String>()

    fun requestToken(contract: Address) {
        contracts.add(contract.address)
    }

    fun requestMain() {
        contracts.add(Address.DEFAULT_API_CONTRACT)
    }
}
