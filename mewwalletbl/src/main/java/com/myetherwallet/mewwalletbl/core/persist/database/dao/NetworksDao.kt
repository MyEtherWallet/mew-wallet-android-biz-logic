package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import com.myetherwallet.mewwalletbl.data.database.EntityNetwork

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class NetworksDao : BaseDao<EntityNetwork> {

    companion object {
        const val TABLE_NAME: String = "networks"
    }
}