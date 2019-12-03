package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityWallet

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class WalletsDao : BaseDao<EntityWallet> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAll(): List<EntityWallet>

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract fun getCount(): Int

    @Query("SELECT id FROM $TABLE_NAME LIMIT 1")
    abstract fun getFirstId(): Long?

    companion object {
        const val TABLE_NAME: String = "wallets"
    }
}
