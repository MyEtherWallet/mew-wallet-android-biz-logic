package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityLocalTransaction

@Dao
abstract class LocalTransactionsDao : BaseDao<EntityLocalTransaction> {

    @Query("SELECT * FROM $TABLE_NAME WHERE hash=:hash")
    abstract suspend fun get(hash: String): EntityLocalTransaction?

    @Query("UPDATE $TABLE_NAME SET was_resend=1 WHERE hash=:txHash")
    abstract suspend fun updateResendState(txHash: String)

    companion object {
        const val TABLE_NAME: String = "local_transactions"
    }
}