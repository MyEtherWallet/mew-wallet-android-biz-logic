package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DexTokensDao

@Entity(tableName = DexTokensDao.TABLE_NAME, indices = [Index(value = ["tokenDescriptionId"], unique = true)])
data class EntityDexToken(
    val tokenDescriptionId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}