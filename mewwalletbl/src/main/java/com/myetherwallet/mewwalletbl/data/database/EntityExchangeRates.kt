package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.CurrencyDao
import java.math.BigDecimal

@Entity(tableName = CurrencyDao.TABLE_NAME, indices = [Index(value = ["fiat"], unique = true)])
class EntityExchangeRates(
        val fiat: String,
        @ColumnInfo(name = "exchange_rate")
        val exchangeRate: BigDecimal
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}