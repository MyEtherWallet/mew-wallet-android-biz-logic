package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityMarket

@Dao
abstract class MarketDao : BaseDao<EntityMarket> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id ASC")
    abstract fun getAll(): List<EntityMarket>

    @Query("DELETE FROM $TABLE_NAME")
    abstract fun deleteAll()

    companion object {
        const val TABLE_NAME: String = "market"
    }
}