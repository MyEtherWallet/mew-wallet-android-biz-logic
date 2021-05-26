package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityRecent
import com.myetherwallet.mewwalletbl.data.database.Recent

@Dao
abstract class RecentDao : BaseDao<EntityRecent> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id DESC")
    abstract suspend fun getAll(): List<Recent>

    companion object {
        const val TABLE_NAME: String = "recent"
    }
}
