package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import com.myetherwallet.mewwalletbl.data.database.EntitySwap
import com.myetherwallet.mewwalletbl.data.database.Swap
import java.util.*

@Dao
abstract class ExchangeDao : BaseDao<EntitySwap> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.id," +
                "$TABLE_NAME.txHash," +
                "$TABLE_NAME.createTime," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.symbol FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.fromDescriptionId) AS fromTokenSymbol," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.logo FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.fromDescriptionId) AS fromTokenLogo ," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.symbol FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.toDescriptionId) AS toTokenSymbol," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.logo FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.toDescriptionId) AS toTokenLogo," +
                "$TABLE_NAME.fromAmount," +
                "$TABLE_NAME.toAmount," +
                "$TABLE_NAME.dex," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.address AS accountAddress," +
                "${PricesDao.TABLE_NAME}.price AS toFiatPrice," +
                "CASE WHEN ${TransactionsDao.TABLE_NAME}.status IS NULL THEN $TABLE_NAME.status ELSE ${TransactionsDao.TABLE_NAME}.status END AS swapStatus," +
                "CASE WHEN ${TransactionsDao.TABLE_NAME}.timestamp IS NULL THEN $TABLE_NAME.updateTime ELSE ${TransactionsDao.TABLE_NAME}.timestamp END AS updateTime, " +
                "$TABLE_NAME.blockchain " +
                "FROM $TABLE_NAME INNER JOIN ${PricesDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} " +
                "ON ${PricesDao.TABLE_NAME}.tokenId=$TABLE_NAME.toDescriptionId " +
                "AND ${AccountsDao.TABLE_NAME}.id=$TABLE_NAME.accountId " +
                "LEFT JOIN ${TransactionsDao.TABLE_NAME} " +
                "ON $TABLE_NAME.txHash=${TransactionsDao.TABLE_NAME}.tx_hash AND $TABLE_NAME.fromDescriptionId=${TransactionsDao.TABLE_NAME}.token_description_id " +
                "WHERE $TABLE_NAME.blockchain=:blockchain " +
                "ORDER BY updateTime DESC"
    )
    abstract suspend fun getAll(blockchain: Blockchain): List<Swap>

    @Query(
        "SELECT " +
                "$TABLE_NAME.id," +
                "$TABLE_NAME.txHash," +
                "$TABLE_NAME.createTime," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.symbol FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.fromDescriptionId) AS fromTokenSymbol," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.logo FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.fromDescriptionId) AS fromTokenLogo ," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.symbol FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.toDescriptionId) AS toTokenSymbol," +
                "(SELECT ${TokenDescriptionDao.TABLE_NAME}.logo FROM ${TokenDescriptionDao.TABLE_NAME} WHERE ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.toDescriptionId) AS toTokenLogo," +
                "$TABLE_NAME.fromAmount," +
                "$TABLE_NAME.toAmount," +
                "$TABLE_NAME.dex," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.address AS accountAddress," +
                "${PricesDao.TABLE_NAME}.price AS toFiatPrice," +
                "CASE WHEN ${TransactionsDao.TABLE_NAME}.status IS NULL THEN $TABLE_NAME.status ELSE ${TransactionsDao.TABLE_NAME}.status END AS swapStatus," +
                "CASE WHEN ${TransactionsDao.TABLE_NAME}.timestamp IS NULL THEN $TABLE_NAME.updateTime ELSE ${TransactionsDao.TABLE_NAME}.timestamp END AS updateTime," +
                "$TABLE_NAME.blockchain " +
                "FROM $TABLE_NAME INNER JOIN ${PricesDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} " +
                "ON ${PricesDao.TABLE_NAME}.tokenId=$TABLE_NAME.toDescriptionId " +
                "AND ${AccountsDao.TABLE_NAME}.id=$TABLE_NAME.accountId " +
                "LEFT JOIN ${TransactionsDao.TABLE_NAME} " +
                "ON $TABLE_NAME.txHash=${TransactionsDao.TABLE_NAME}.tx_hash " +
                "WHERE $TABLE_NAME.txHash=:hash " +
                "LIMIT 1"
    )
    abstract suspend fun getSwap(hash: String): Swap?

    @Query("UPDATE $TABLE_NAME SET txHash=:hash WHERE id=:id")
    abstract suspend fun updateTxHashById(id: Long, hash: String)

    @Query("UPDATE $TABLE_NAME SET txHash=:newTxHash WHERE txHash=:oldTxHash")
    abstract suspend fun updateTxHash(oldTxHash: String, newTxHash: String)

    @Query("UPDATE $TABLE_NAME SET status=:status, updateTime=:timestamp WHERE id=:swapId")
    abstract suspend fun updateStatusById(swapId: Long, status: TransactionStatus, timestamp: Date)

    @Query("UPDATE $TABLE_NAME SET status=:status, updateTime=:timestamp WHERE txHash=:txHash")
    abstract suspend fun updateStatusByHash(txHash: String, status: TransactionStatus, timestamp: Date)

    companion object {
        const val TABLE_NAME: String = "exchange"
    }
}