package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DexPricesDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.util.*

@Entity(tableName = DexPricesDao.TABLE_NAME, indices = [Index(value = ["blockchain","exchange","dex","base","quote","scale"], unique = true)])
data class EntityDexPrice(
    val blockchain: Blockchain,
    val exchange: String,
    val dex: String,
    val price: BigDecimal,
    val base: Address,
    val quote: Address,
    val scale: Int,
    val timestamp: Date,
    val marketImpact: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}