package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.MarketDao
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.util.*

@Entity(tableName = MarketDao.TABLE_NAME, indices = [Index(value = ["contract"], unique = true)])
data class EntityMarket(
    val contract: Address,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val price: String,
    val logo: String,
    val website: String,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}