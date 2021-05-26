package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.PricesDao
import java.math.BigDecimal
import java.util.*

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = PricesDao.TABLE_NAME, indices = [Index(value = ["tokenId"], unique = true)])
data class EntityPrice(
    val tokenId: Long,
    val price: BigDecimal,
    val sparkline: String,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
