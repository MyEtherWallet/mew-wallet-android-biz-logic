package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityLocalTransaction

@Dao
abstract class LocalTransactionsDao : BaseDao<EntityLocalTransaction> {

    @Query("SELECT * FROM $TABLE_NAME WHERE hash=:hash")
    abstract suspend fun get(hash: String) : EntityLocalTransaction?

    companion object {
        const val TABLE_NAME: String = "local_transactions"
    }
}