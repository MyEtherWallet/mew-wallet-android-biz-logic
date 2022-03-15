package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.EntitySwapSearchHistory
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class SwapSearchHistoryDao : BaseDao<EntitySwapSearchHistory> {

    @Query("SELECT contract FROM $TABLE_NAME WHERE blockchain=:blockchain ORDER BY timestamp DESC")
    abstract suspend fun getAll(blockchain: Blockchain): List<Address>

    companion object {
        const val TABLE_NAME: String = "swap_search_history"
    }
}