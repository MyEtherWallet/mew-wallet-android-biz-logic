package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.TransactionsDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = TransactionsDao.TABLE_NAME, indices = [Index(value = ["account_id","from_recipient_id","nonce","blockchain"], unique = true)])
data class EntityTransaction(
    @ColumnInfo(name = "account_id")
    val accountId: Long,
    @ColumnInfo(name = "from_recipient_id")
    val fromRecipientId: Long,
    @ColumnInfo(name = "to_recipient_id")
    val toRecipientId: Long,
    @ColumnInfo(name = "token_description_id")
    val tokenDescriptionId: Long,
    val amount: Double,
    val status: TransactionStatus,
    val timestamp: Date,
    @ColumnInfo(name = "tx_hash")
    val txHash: String,
    val nonce: BigInteger,
    val blockchain: Blockchain
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
