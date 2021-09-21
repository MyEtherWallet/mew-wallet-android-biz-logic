package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.LocalTransactionsDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.addHexPrefix
import com.myetherwallet.mewwalletkit.core.extension.encode
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
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
        transaction.gasPrice,
        transaction.encode()?.toHexString()?.addHexPrefix(),
        0
    )
}