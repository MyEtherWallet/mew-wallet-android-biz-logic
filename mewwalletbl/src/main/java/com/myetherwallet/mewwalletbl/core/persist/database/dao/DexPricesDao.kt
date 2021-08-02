package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.EntityDexPrice
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.util.*

@Dao
abstract class DexPricesDao : BaseDao<EntityDexPrice> {

    @Query(
        "SELECT * " +
                "FROM $TABLE_NAME " +
                "WHERE blockchain=:blockchain AND base=:from AND quote=:to AND scale=:scale AND timestamp>:expirationDate " +
                "ORDER BY price DESC"
    )
    abstract suspend fun getPrices(blockchain: Blockchain, from: Address, to: Address, scale: Int, expirationDate: Date): List<EntityDexPrice>

    @Query(
        "SELECT MAX(price) " +
                "FROM $TABLE_NAME " +
                "WHERE blockchain=:blockchain AND base=:from AND quote=:to AND timestamp>:expirationDate"
    )
    abstract suspend fun getBestPrice(blockchain: Blockchain, from: Address, to: Address, expirationDate: Date): Double?

    @Query(
        "DELETE FROM $TABLE_NAME " +
                "WHERE blockchain=:blockchain AND base=:from AND quote=:to AND scale=:scale"
    )
    abstract suspend fun deletePair(blockchain: Blockchain, from: Address, to: Address, scale: Int)

    companion object {
        const val TABLE_NAME: String = "dex_prices"
    }

}