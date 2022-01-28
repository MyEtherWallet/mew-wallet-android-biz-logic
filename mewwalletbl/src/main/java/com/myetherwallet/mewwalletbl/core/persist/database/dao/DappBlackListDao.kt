package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityDappBlackList

/**
 * Created by BArtWell on 18.11.2021.
 */

@Dao
abstract class DappBlackListDao : BaseDao<EntityDappBlackList> {

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract suspend fun getCount(): Int

    @Query("SELECT id FROM $TABLE_NAME WHERE domain=:domain")
    abstract suspend fun get(domain: String): Int?

    @Query("DELETE FROM $TABLE_NAME")
    abstract suspend fun clear()

    companion object {
        const val TABLE_NAME: String = "dapp_black_list"
    }
}
