package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import java.util.*

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = "transactions", indices = [Index(value = ["txHash","tokenDescriptionId","fromRecipientId","toRecipientId"], unique = true)])
data class EntityTransaction(
    val accountId: Long,
    val fromRecipientId: Long,
    val toRecipientId: Long,
    val tokenDescriptionId: Long,
    val amount: Double,
    val status: TransactionStatus,
    val timestamp: Date,
    val txHash: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
