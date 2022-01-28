package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.DappBlackListDao

/**
 * Created by BArtWell on 18.11.2021.
 */

@Entity(tableName = DappBlackListDao.TABLE_NAME)
class EntityDappBlackList(val domain: String) {

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
