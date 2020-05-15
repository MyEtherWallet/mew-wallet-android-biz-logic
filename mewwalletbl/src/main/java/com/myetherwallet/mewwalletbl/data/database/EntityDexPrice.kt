package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DexPricesDao
import java.util.*

@Entity(tableName = DexPricesDao.TABLE_NAME, indices = [Index(value = ["dex","base","quote","scale"], unique = true)])
data class EntityDexPrice(
    val dex: String,
    val price: Double,
    val base: Long,
    val quote: Long,
    val scale: Int,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}