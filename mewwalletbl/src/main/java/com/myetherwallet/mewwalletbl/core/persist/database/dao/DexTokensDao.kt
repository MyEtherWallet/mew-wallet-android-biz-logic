package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityDexToken
import com.myetherwallet.mewwalletbl.data.database.TokenDescription
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class DexTokensDao : BaseDao<EntityDexToken> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAll(): List<EntityDexToken>

    @Query("SELECT " +
            "${TokenDescriptionDao.TABLE_NAME}.id," +
            "${TokenDescriptionDao.TABLE_NAME}.decimals," +
            "${TokenDescriptionDao.TABLE_NAME}.name," +
            "${TokenDescriptionDao.TABLE_NAME}.symbol AS symbol," +
            "${TokenDescriptionDao.TABLE_NAME}.logo," +
            "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
            "CASE WHEN IFNULL(${TokenDescriptionDao.TABLE_NAME}.logo,'')='' THEN 1 ELSE 0 END AS isLogo," +
            "CASE WHEN ${TokenDescriptionDao.TABLE_NAME}.address='' THEN 0 ELSE 1 END AS isDefault " +
            "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
            "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
            "WHERE ${TokenDescriptionDao.TABLE_NAME}.symbol IS NOT '' " +
            "ORDER BY isDefault,$TABLE_NAME.id"
    )
    abstract fun getAllTokenDescription(): List<TokenDescription>

    @Query("SELECT " +
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
    abstract fun getTokenDescription(address: Address): TokenDescription?

    @Query("DELETE FROM $TABLE_NAME")
    abstract fun deleteAll()

    companion object {
        const val TABLE_NAME: String = "dex_tokens"
    }
}