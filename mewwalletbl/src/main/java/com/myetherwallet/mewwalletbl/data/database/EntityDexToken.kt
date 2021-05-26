package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DexTokensDao

@Entity(tableName = DexTokensDao.TABLE_NAME, indices = [Index(value = ["tokenDescriptionId"], unique = true)])
data class EntityDexToken(
    val tokenDescriptionId: Long,
    val volume_24h: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}