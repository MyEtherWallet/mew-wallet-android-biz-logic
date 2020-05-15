package com.myetherwallet.mewwalletbl.data.database

import com.myetherwallet.mewwalletbl.MewEnvironment
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import java.util.*

data class TransactionInfo(
    val hash: String,
    val address: String,
    val fromRecipient: String,
    val toRecipient: String,
    val amount: Double,
    val status: TransactionStatus,
    val timestamp: Date,
    val tokenName: String,
    val symbol: String,
    val logo: String?,
    val price: Double
) {

    fun getUrl() = if (MewEnvironment.current == MewEnvironment.Type.ROPSTEN) {
        "https://ropsten.etherscan.io/tx/$hash"
    } else {
        "https://etherscan.io/tx/$hash"
    }
}