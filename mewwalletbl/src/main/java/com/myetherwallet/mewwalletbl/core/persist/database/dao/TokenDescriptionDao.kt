package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.EntityTokenDescription
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class TokenDescriptionDao : BaseDao<EntityTokenDescription> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityTokenDescription>

    @Query("SELECT * FROM $TABLE_NAME WHERE id=:id")
    abstract suspend fun get(id: Long): EntityTokenDescription?

    @Query("SELECT * FROM $TABLE_NAME WHERE blockchain=:blockchain AND address=:address")
    abstract suspend fun get(address: Address, blockchain: Blockchain): EntityTokenDescription?

    suspend fun getExistsIdOrInsert(entity: EntityTokenDescription): Long {
        val id = Database.instance.getTokenDescriptionDao().insertOrIgnore(entity)
        return if (id == -1L) {
            Database.instance.getTokenDescriptionDao().get(entity.address, entity.blockchain)!!.id
        } else {
            id
        }
    }

    companion object {
        const val TABLE_NAME: String = "tokens_descriptions"
    }
}