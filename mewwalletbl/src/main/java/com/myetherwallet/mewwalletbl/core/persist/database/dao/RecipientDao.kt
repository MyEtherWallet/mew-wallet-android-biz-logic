package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.data.database.EntityRecipient
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class RecipientDao : BaseDao<EntityRecipient> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityRecipient>

    @Query("SELECT id FROM $TABLE_NAME WHERE address=:address")
    abstract suspend fun get(address: Address): Long?

    suspend fun getExistsIdOrInsert(address: Address): Long {
        val id = Database.instance.getRecipientDao().insertOrIgnore(EntityRecipient(address))
        return if (id == -1L) {
            Database.instance.getRecipientDao().get(address)!!
        } else {
            id
        }
    }

    companion object {
        const val TABLE_NAME: String = "recipients"
    }
}