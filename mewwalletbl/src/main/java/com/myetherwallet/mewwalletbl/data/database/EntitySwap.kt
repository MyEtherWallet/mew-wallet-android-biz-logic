package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.ExchangeDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.util.*

@Entity(tableName = ExchangeDao.TABLE_NAME)
data class EntitySwap(
    val txHash: String?,
    val createTime: Date,
    val updateTime: Date,
    val fromDescriptionId: Long,
    val toDescriptionId: Long,
    val fromAmount: Double,
    val toAmount: Double,
    val accountId: Long,
    val toAddress: Address,
    val dex: String,
    val status: TransactionStatus,
    val blockchain: Blockchain
    ) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}