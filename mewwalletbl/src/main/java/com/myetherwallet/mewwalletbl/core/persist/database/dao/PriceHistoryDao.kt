package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.myetherwallet.mewwalletbl.data.database.EntityPriceHistory
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class PriceHistoryDao : BaseDao<EntityPriceHistory> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityPriceHistory>

    @Query("SELECT * FROM $TABLE_NAME WHERE contract=:contract AND interval=:interval ORDER BY id ASC")
    abstract suspend fun get(contract: Address, interval: String): List<EntityPriceHistory>

    @Query("DELETE FROM $TABLE_NAME WHERE contract=:contract AND interval=:interval")
    abstract suspend fun delete(contract: Address, interval: String) : Int

    @Transaction
    open suspend fun deleteAndInsertList(contract: Address, interval: String, entityList: List<EntityPriceHistory>) {
        delete(contract, interval)
        entityList.forEach {
            insert(it)
        }
    }

    companion object {
        const val TABLE_NAME: String = "price_history"
    }
}