package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.BalanceInfo
import com.myetherwallet.mewwalletbl.data.database.EntityBalance
import com.myetherwallet.mewwalletbl.data.database.ExtBalanceInfo
import com.myetherwallet.mewwalletbl.data.database.TotalBalance

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class BalancesDao : BaseDao<EntityBalance> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${PricesDao.TABLE_NAME}.price " +
                "FROM $TABLE_NAME INNER JOIN ${TokensDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} INNER JOIN ${PricesDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenId=${TokensDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND ${TokenDescriptionDao.TABLE_NAME}.id=${PricesDao.TABLE_NAME}.tokenId " +
                "GROUP BY $TABLE_NAME.tokenId ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract fun getTokensBalance(): List<TotalBalance>

    @Query(
        "SELECT " +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${TABLE_NAME}.timestamp," +
                "${AccountsDao.TABLE_NAME}.address," +
                "${PricesDao.TABLE_NAME}.price " +
                "FROM $TABLE_NAME INNER JOIN ${TokensDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
                "AND $TABLE_NAME.tokenId=${TokensDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${TokenDescriptionDao.TABLE_NAME}.id=${PricesDao.TABLE_NAME}.tokenId " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address " +
                "GROUP BY contract ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract fun getLast(address: String): List<BalanceInfo>

    @Query(
        "SELECT " +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${TABLE_NAME}.timestamp," +
                "${AccountsDao.TABLE_NAME}.address," +
                "${PricesDao.TABLE_NAME}.price," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.hide, " +
                "${AccountsDao.TABLE_NAME}.position " +
                "FROM $TABLE_NAME INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokensDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
                "AND $TABLE_NAME.tokenId=${TokensDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${TokenDescriptionDao.TABLE_NAME}.id=${PricesDao.TABLE_NAME}.tokenId " +
                "WHERE ${TokensDao.TABLE_NAME}.isPrimary=1 " +
                "GROUP BY ${AccountsDao.TABLE_NAME}.address " +
                "ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract fun getPrimaryBalances(): List<ExtBalanceInfo>

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAll(): List<EntityBalance>

    companion object {
        const val TABLE_NAME: String = "balances"
    }
}
