package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.LocalTransactionsDao
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.addHexPrefix
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.eip.eip155.LegacyTransaction
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import com.myetherwallet.mewwalletkit.eip.eip1559.Eip1559Transaction
import java.math.BigInteger

@Entity(tableName = LocalTransactionsDao.TABLE_NAME, indices = [Index(value = ["hash"], unique = true)])
class EntityLocalTransaction(
    val hash: String,
    val nonce: BigInteger,
    @ColumnInfo(name = "from_address")
    val from: Address,
    @ColumnInfo(name = "to_address")
    val to: Address,
    val value: BigInteger,
    val input: String,
    val gas: BigInteger,
    @ColumnInfo(name = "gas_price")
    val gasPrice: BigInteger,
    @ColumnInfo(name = "max_fee_per_gas", defaultValue = "")
    val maxFeePerGas: BigInteger,
    @ColumnInfo(name = "max_priority_fee_per_gas", defaultValue = "")
    val maxPriorityFeePerGas: BigInteger,
    @ColumnInfo(name = "signed_tx")
    val signedTx: String?,
    @ColumnInfo(name = "was_resend")
    val wasResend: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(txHash: String, transaction: Transaction) : this(
        txHash,
        transaction.nonce,
        transaction.from!!,
        transaction.to!!,
        transaction.value,
        transaction.data.toHexString(),
        transaction.gasLimit,
        if (transaction is LegacyTransaction) transaction.gasPrice else BigInteger.ZERO,
        if (transaction is Eip1559Transaction) transaction.maxFeePerGas else BigInteger.ZERO,
        if (transaction is Eip1559Transaction) transaction.maxPriorityFeePerGas else BigInteger.ZERO,
        transaction.serialize()?.toHexString()?.addHexPrefix(),
        0
    )
}