package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.BalanceInfo
import com.myetherwallet.mewwalletbl.data.database.EntityBalance
import com.myetherwallet.mewwalletbl.data.database.ExtBalanceInfo
import com.myetherwallet.mewwalletbl.data.database.TotalBalance
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class BalancesDao : BaseDao<EntityBalance> {

    @Query(
        "SELECT " +
                "${AccountsDao.TABLE_NAME}.id," +
                "${TokenDescriptionDao.TABLE_NAME}.id AS descriptionId," +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${TABLE_NAME}.timestamp," +
                "${AccountsDao.TABLE_NAME}.address," +
                "${AccountsDao.TABLE_NAME}.eth2_address," +
                "IFNULL(${PricesDao.TABLE_NAME}.price, 0) AS price," +
                "${PricesDao.TABLE_NAME}.sparkline," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.hide, " +
                "${AccountsDao.TABLE_NAME}.position," +
                "${TokensDao.TABLE_NAME}.isHidden AS isHiddenToken," +
                "${AccountsDao.TABLE_NAME}.anonymous_id " +
                "FROM $TABLE_NAME INNER JOIN ${TokensDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
                "AND $TABLE_NAME.tokenId=${TokensDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${TokenDescriptionDao.TABLE_NAME}.id=${PricesDao.TABLE_NAME}.tokenId " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address AND ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain"
    )
    abstract suspend fun getLast(blockchain: Blockchain, address: String): List<ExtBalanceInfo>

    @Query(
        "SELECT " +
                "${AccountsDao.TABLE_NAME}.id," +
                "${TokenDescriptionDao.TABLE_NAME}.id AS descriptionId," +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${TABLE_NAME}.timestamp," +
                "${AccountsDao.TABLE_NAME}.address," +
                "${AccountsDao.TABLE_NAME}.eth2_address," +
                "IFNULL(${PricesDao.TABLE_NAME}.price, 0) AS price," +
                "${PricesDao.TABLE_NAME}.sparkline," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.hide, " +
                "${AccountsDao.TABLE_NAME}.position," +
                "${TokensDao.TABLE_NAME}.isHidden AS isHiddenToken," +
                "${AccountsDao.TABLE_NAME}.anonymous_id " +
                "FROM $TABLE_NAME INNER JOIN ${TokensDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
                "AND $TABLE_NAME.tokenId=${TokensDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${TokenDescriptionDao.TABLE_NAME}.id=${PricesDao.TABLE_NAME}.tokenId " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address AND ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain AND contract=:contract"
    )
    abstract suspend fun getLast(blockchain: Blockchain, address: Address, contract: Address): List<ExtBalanceInfo>

    @Query(
        "SELECT " +
                "${AccountsDao.TABLE_NAME}.id," +
                "${TokenDescriptionDao.TABLE_NAME}.id AS descriptionId," +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${TokenDescriptionDao.TABLE_NAME}.name," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${TABLE_NAME}.timestamp AS timestamp," +
                "${AccountsDao.TABLE_NAME}.address," +
                "${AccountsDao.TABLE_NAME}.eth2_address," +
                "IFNULL(${PricesDao.TABLE_NAME}.price, 0) AS price," +
                "${PricesDao.TABLE_NAME}.sparkline," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.hide, " +
                "${AccountsDao.TABLE_NAME}.position," +
                "${TokensDao.TABLE_NAME}.isHidden AS isHiddenToken," +
                "${AccountsDao.TABLE_NAME}.anonymous_id " +
                "FROM $TABLE_NAME INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokensDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
                "AND $TABLE_NAME.tokenId=${TokensDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${TokenDescriptionDao.TABLE_NAME}.id=${PricesDao.TABLE_NAME}.tokenId " +
                "WHERE ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain AND ${TokensDao.TABLE_NAME}.isPrimary=1"
    )
    abstract suspend fun getPrimaryBalances(blockchain: Blockchain): List<ExtBalanceInfo>

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityBalance>

    @Query("DELETE FROM $TABLE_NAME WHERE tokenId=:tokenId")
    abstract suspend fun removeTokenBalance(tokenId: Long)

    companion object {
        const val TABLE_NAME: String = "balances"
    }
}
