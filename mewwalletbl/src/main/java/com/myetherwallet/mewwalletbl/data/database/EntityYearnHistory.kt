package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.YearnHistoryDao
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnType
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

@Entity(tableName = YearnHistoryDao.TABLE_NAME)
class EntityYearnHistory(
    @ColumnInfo(name = "account_address")
    val accountAddress: Address,
    @ColumnInfo(name = "type")
    val type: YearnType,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "contract_address")
    val contractAddress: Address,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "price")
    val price: BigDecimal,
    @ColumnInfo(name = "amount")
    val amount: BigDecimal,
    @ColumnInfo(name = "decimals")
    val decimals: Int,
    @ColumnInfo(name = "timestamp")
    val timestamp: Date,
    @ColumnInfo(name = "tx_hash")
    val txHash: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}