package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityMarketFavorite
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class MarketFavoriteDao : BaseDao<EntityMarketFavorite> {


    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityMarketFavorite>

    @Query("SELECT * FROM $TABLE_NAME  WHERE contract_address=:contract LIMIT 1")
    abstract suspend fun get(contract: Address): EntityMarketFavorite?

    @Query("DELETE FROM $TABLE_NAME WHERE contract_address=:contract")
    abstract suspend fun delete(contract: Address)

    companion object {
        const val TABLE_NAME: String = "market_favorite"
    }
}
