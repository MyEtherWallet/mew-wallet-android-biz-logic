package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.myetherwallet.mewwalletbl.data.database.EntityDexToken
import com.myetherwallet.mewwalletbl.data.database.EntityPriceHistory
import com.myetherwallet.mewwalletbl.data.database.ExtTokenDescription
import com.myetherwallet.mewwalletbl.data.database.TokenDescription
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class DexTokensDao : BaseDao<EntityDexToken> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityDexToken>

    @Query(
        "SELECT " +
                "${TokenDescriptionDao.TABLE_NAME}.id," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol AS symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${PricesDao.TABLE_NAME}.price," +
                "$TABLE_NAME.volume_24h " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${PricesDao.TABLE_NAME}.tokenId " +
                "WHERE ${TokenDescriptionDao.TABLE_NAME}.symbol IS NOT '' " +
                "ORDER BY $TABLE_NAME.id"
    )
    abstract suspend fun getAllTokenDescription(): List<ExtTokenDescription>

    @Query(
        "SELECT " +
                "${TokenDescriptionDao.TABLE_NAME}.id," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol AS symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE ${TokenDescriptionDao.TABLE_NAME}.address=:address"
    )
    abstract suspend fun getTokenDescription(address: Address): TokenDescription?

    @Query("DELETE FROM $TABLE_NAME")
    abstract suspend fun deleteAll()

    @Transaction
    open suspend fun deleteAndInsertList(entityList: List<EntityDexToken>) {
        deleteAll()
        entityList.forEach {
            insertOrIgnore(it)
        }
    }

    companion object {
        const val TABLE_NAME: String = "dex_tokens"
    }
}