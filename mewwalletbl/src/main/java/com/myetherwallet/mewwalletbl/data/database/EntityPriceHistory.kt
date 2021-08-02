package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.PriceHistoryDao
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.util.*

@Entity(tableName = PriceHistoryDao.TABLE_NAME)
data class EntityPriceHistory(
    val contract: Address,
    val interval: String,
    val price: BigDecimal,
    val volume: BigDecimal,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}