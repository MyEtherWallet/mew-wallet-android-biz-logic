package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.PurchaseDao
import com.myetherwallet.mewwalletbl.data.PurchaseState
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

@Entity(tableName = PurchaseDao.TABLE_NAME, indices = [Index(value = ["transactionId"], unique = true)])
data class EntityPurchase (
    val transactionId: String,
    val fiatAmount: BigDecimal,
    val fiatCurrency: String,
    val cryptoAmount: BigDecimal,
    val cryptoDescriptionId: Long,
    val timestamp: Date,
    val status: PurchaseState,
    val provider: String)
{
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}