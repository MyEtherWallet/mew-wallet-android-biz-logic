package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.LidoHistoryDao

@Entity(tableName = LidoHistoryDao.TABLE_NAME, indices = [Index(value = ["tx_hash"], unique = true)])
class EntityLidoHistory(
    @ColumnInfo(name = "tx_hash")
    val txHash: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}