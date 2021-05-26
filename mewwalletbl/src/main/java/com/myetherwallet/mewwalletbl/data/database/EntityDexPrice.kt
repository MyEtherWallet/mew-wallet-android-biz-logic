package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DexPricesDao
import java.math.BigDecimal
import java.util.*

@Entity(tableName = DexPricesDao.TABLE_NAME, indices = [Index(value = ["exchange","dex","base","quote","scale"], unique = true)])
data class EntityDexPrice(
    val exchange: String,
    val dex: String,
    val price: BigDecimal,
    val base: Long,
    val quote: Long,
    val scale: Int,
    val timestamp: Date,
    val marketImpact: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}