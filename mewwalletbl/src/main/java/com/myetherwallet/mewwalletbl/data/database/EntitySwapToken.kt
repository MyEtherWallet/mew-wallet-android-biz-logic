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

@Entity(tableName = SwapTokensDao.TABLE_NAME, indices = [Index(value = ["blockchain", "contract_address"], unique = true)])
data class EntitySwapToken(
    val blockchain: Blockchain,
    @ColumnInfo(name = "contract_address")
    val contractAddress: Address,
    val name: String,
    val symbol: String,
    val icon: String,
    val decimals: Int,
    val timestamp: Date,
    val price: BigDecimal,
    @ColumnInfo(name = "volume_24h")
    val volume24h: BigDecimal
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}