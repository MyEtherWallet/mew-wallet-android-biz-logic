package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityDexPrice
import java.util.*

@Dao
abstract class DexPricesDao : BaseDao<EntityDexPrice> {

    @Query(
        "SELECT * " +
                "FROM $TABLE_NAME " +
                "WHERE base=:from AND quote=:to AND scale=:scale AND timestamp>:expirationDate " +
                "ORDER BY price DESC"
    )
    abstract suspend fun getPrices(from: Long, to: Long, scale: Int, expirationDate: Date): List<EntityDexPrice>

    @Query(
        "SELECT MAX(price) " +
                "FROM $TABLE_NAME " +
                "WHERE base=:from AND quote=:to AND timestamp>:expirationDate"
    )
    abstract suspend fun getBestPrice(from: Long, to: Long, expirationDate: Date): Double?

    @Query(
        "DELETE FROM $TABLE_NAME " +
                "WHERE base=:from AND quote=:to AND scale=:scale"
    )
    abstract suspend fun deletePair(from: Long, to: Long, scale: Int)

    companion object {
        const val TABLE_NAME: String = "dex_prices"
    }

}