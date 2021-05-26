package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityExchangeRates

@Dao
abstract class CurrencyDao : BaseDao<EntityExchangeRates> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAll(): List<EntityExchangeRates>

    @Query("SELECT * FROM $TABLE_NAME WHERE fiat = :fiat")
    abstract fun get(fiat: String): EntityExchangeRates?

    @Query("SELECT * FROM $TABLE_NAME WHERE fiat IN (:fiats)")
    abstract fun get(fiats: List<String>): List<EntityExchangeRates>?

    @Query("DELETE FROM $TABLE_NAME")
    abstract fun clear()

    companion object {
        const val TABLE_NAME = "currency"
    }
}
