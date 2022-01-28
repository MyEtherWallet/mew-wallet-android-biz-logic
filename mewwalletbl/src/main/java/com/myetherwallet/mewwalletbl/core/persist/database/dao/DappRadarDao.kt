package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityDappRadar

@Dao
abstract class DappRadarDao : BaseDao<EntityDappRadar> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityDappRadar>

    @Query("DELETE FROM $TABLE_NAME")
    abstract suspend fun clear()

    companion object {
        const val TABLE_NAME: String = "dapp_radar"
    }
}
