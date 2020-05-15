package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.RecentDao
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Entity(tableName = RecentDao.TABLE_NAME, indices = [Index(value = ["address"], unique = true)])
data class EntityRecent(
    val address: Address
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
