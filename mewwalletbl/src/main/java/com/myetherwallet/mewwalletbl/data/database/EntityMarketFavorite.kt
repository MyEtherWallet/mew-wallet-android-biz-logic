package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.MarketFavoriteDao
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Entity(tableName = MarketFavoriteDao.TABLE_NAME)
data class EntityMarketFavorite(
    @ColumnInfo(name = "contract_address")
    val contractAddress: Address,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
