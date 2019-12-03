package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.WalletsDao

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = WalletsDao.TABLE_NAME)
data class EntityWallet(
    val isBackedUp: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}