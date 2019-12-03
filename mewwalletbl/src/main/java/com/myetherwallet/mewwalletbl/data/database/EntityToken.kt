package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.TokensDao

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = TokensDao.TABLE_NAME, indices = [Index(value = ["accountId","tokenDescriptionId"], unique = true)])
class EntityToken(
    val accountId: Long,
    val tokenDescriptionId: Long,
    val isPrimary: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}