package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnType
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

@Parcelize
class YearnVaultInfo(
    val yearnType: YearnType,
    val accountAddress: Address,
    val accountName: String,
    val accountPosition: Int,
    val toAddress: Address,
    val contractAddress: Address,
    val symbol: String,
    val tokenName: String,
    val icon: String,
    val amount: BigDecimal,
    val decimals: Int,
    val price: BigDecimal?,
    val txHash: String,
    val timestamp: Date,
    val status: TransactionStatus,
    val nonce: BigInteger,
    val blockchain: Blockchain
) : Parcelable {

    fun toTransactionInfo() = with(this) {
        TransactionInfo(
            txHash,
            accountAddress,
            accountAddress,
            toAddress,
            amount,
            status,
            timestamp,
            tokenName,
            symbol,
            icon,
            contractAddress,
            price,
            nonce,
            blockchain
        )
    }
}