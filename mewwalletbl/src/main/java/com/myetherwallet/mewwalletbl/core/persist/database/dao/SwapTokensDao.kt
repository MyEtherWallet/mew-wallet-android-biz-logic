package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.EntitySwapToken
import com.myetherwallet.mewwalletbl.data.database.SwapTokenDescription
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class SwapTokensDao : BaseDao<EntitySwapToken> {

    @Query("SELECT * FROM $TABLE_NAME WHERE blockchain=:blockchain")
    abstract suspend fun getAll(blockchain: Blockchain): List<SwapTokenDescription>

    @Query("SELECT * FROM $TABLE_NAME WHERE blockchain=:blockchain AND contract_address=:contract")
    abstract suspend fun get(blockchain: Blockchain, contract: Address): SwapTokenDescription?

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE blockchain=:blockchain")
    abstract suspend fun getCount(blockchain: Blockchain): Int

    @Query("DELETE FROM $TABLE_NAME WHERE blockchain=:blockchain")
    abstract suspend fun deleteAll(blockchain: Blockchain)

    @Transaction
    open suspend fun deleteAndInsertList(blockchain: Blockchain, entityList: List<EntitySwapToken>) {
        deleteAll(blockchain)
        entityList.forEach { insertOrReplace(it) }
    }

    companion object {
        const val TABLE_NAME: String = "swap_tokens"
    }
}