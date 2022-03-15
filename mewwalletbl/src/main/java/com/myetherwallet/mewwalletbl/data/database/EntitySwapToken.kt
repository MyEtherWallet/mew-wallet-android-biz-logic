package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.SwapTokensDao
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.util.*

@Entity(tableName = SwapTokensDao.TABLE_NAME, indices = [Index(value = ["blockchain", "contract_address","category"], unique = true)])
data class EntitySwapToken(
    @ColumnInfo(name = "blockchain", defaultValue = "ETHEREUM")
    val blockchain: Blockchain,
    @ColumnInfo(name = "contract_address", defaultValue = "")
    val contractAddress: Address,
    @ColumnInfo(name = "name", defaultValue = "")
    val name: String,
    @ColumnInfo(name = "symbol", defaultValue = "")
    val symbol: String,
    @ColumnInfo(name = "icon", defaultValue = "")
    val icon: String,
    @ColumnInfo(name = "decimals", defaultValue = "18")
    val decimals: Int,
    @ColumnInfo(name = "timestamp", defaultValue = "0")
    val timestamp: Date,
    @ColumnInfo(name = "price", defaultValue = "0")
    val price: BigDecimal,
    @ColumnInfo(name = "volume_24h", defaultValue = "0")
    val volume24h: BigDecimal,
    @ColumnInfo(name = "category", defaultValue = "TOKENS")
    val category: SwapTokenCategory
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}