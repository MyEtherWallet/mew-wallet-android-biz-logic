package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.data.database.EntityTokenDescription
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class TokenDescriptionDao : BaseDao<EntityTokenDescription> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityTokenDescription>

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:id")
    abstract suspend fun get(id: Long): EntityTokenDescription?

    @Query("SELECT * FROM $TABLE_NAME WHERE address=:address")
    abstract suspend fun get(address: Address): EntityTokenDescription?

    @Query("SELECT * FROM $TABLE_NAME WHERE symbol=:symbol")
    abstract suspend fun get(symbol: String): EntityTokenDescription?

    suspend fun getExistsIdOrInsert(entity: EntityTokenDescription): Long {
        val id = Database.instance.getTokenDescriptionDao().insertOrIgnore(entity)
        return if (id == -1L) {
            Database.instance.getTokenDescriptionDao().get(entity.address)!!.id
        } else {
            id
        }
    }

    companion object {
        const val TABLE_NAME: String = "tokens_descriptions"
    }
}