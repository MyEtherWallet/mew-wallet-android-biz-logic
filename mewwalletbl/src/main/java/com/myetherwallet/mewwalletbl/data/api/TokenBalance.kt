package com.myetherwallet.mewwalletbl.data.api

import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.util.*

data class TokenBalance(
    val contract_address: Address,
    val amount: String,
    val timestamp: Date
) {

    fun getContractAddress() = if (contract_address.address == Address.DEFAULT_API_CONTRACT) {
        Address.createDefault()
    } else {
        contract_address
    }
}
