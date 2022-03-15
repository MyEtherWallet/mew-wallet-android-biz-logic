package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.SwapSearchHistoryDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.util.*

@Entity(tableName = SwapSearchHistoryDao.TABLE_NAME, indices = [Index(value = ["blockchain","contract"], unique = true)])
data class EntitySwapSearchHistory(
    val blockchain: Blockchain,
    val contract: Address,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}