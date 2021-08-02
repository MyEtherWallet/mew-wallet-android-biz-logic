package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityStakedInfo

@Dao
abstract class StakedHistoryDao : BaseDao<EntityStakedInfo> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY timestamp DESC")
    abstract suspend fun getAll(): List<EntityStakedInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE request_uuid=:uuid")
    abstract suspend fun get(uuid: String): EntityStakedInfo?

    companion object {
        const val TABLE_NAME: String = "staked_history"
    }
}