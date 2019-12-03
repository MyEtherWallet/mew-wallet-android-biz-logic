package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.NetworksDao

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = NetworksDao.TABLE_NAME)
data class EntityNetwork(
    val accountId: Long,
    val derivationPath: String,
    val name: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}