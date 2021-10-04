package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityYearnHistory
import com.myetherwallet.mewwalletbl.data.database.YearnVaultInfo

@Dao
abstract class YearnHistoryDao : BaseDao<EntityYearnHistory> {

    @Query("SELECT * FROM $TABLE_NAME ORDER BY timestamp DESC")
    abstract suspend fun getAll(): List<EntityYearnHistory>

    @Query(
        "SELECT * FROM $TABLE_NAME INNER JOIN ${TransactionsDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tx_hash=${TransactionsDao.TABLE_NAME}.tx_hash " +
                "WHERE $TABLE_NAME.tx_hash IS NOT NULL AND ${TransactionsDao.TABLE_NAME}.status='PENDING' " +
                "ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract suspend fun getPending(): List<EntityYearnHistory>

    @Query(
        "SELECT " +
                "$TABLE_NAME.type AS yearnType," +

                "${AccountsDao.TABLE_NAME}.address AS accountAddress," +
                "${AccountsDao.TABLE_NAME}.name AS accountName," +
                "${AccountsDao.TABLE_NAME}.position AS accountPosition," +

                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=${TransactionsDao.TABLE_NAME}.to_recipient_id) AS toAddress," +

                "$TABLE_NAME.contract_address AS contractAddress," +
                "$TABLE_NAME.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "$TABLE_NAME.icon," +
                "$TABLE_NAME.amount," +
                "${TokenDescriptionDao.TABLE_NAME}.decimals," +
                "${PricesDao.TABLE_NAME}.price," +

                "${TransactionsDao.TABLE_NAME}.tx_hash AS txHash," +
                "${TransactionsDao.TABLE_NAME}.timestamp," +
                "${TransactionsDao.TABLE_NAME}.status," +
                "${TransactionsDao.TABLE_NAME}.nonce," +
                "${TransactionsDao.TABLE_NAME}.blockchain " +

                "FROM $TABLE_NAME INNER JOIN ${TransactionsDao.TABLE_NAME} ON $TABLE_NAME.tx_hash=${TransactionsDao.TABLE_NAME}.tx_hash "+
                "INNER JOIN ${AccountsDao.TABLE_NAME} ON ${TransactionsDao.TABLE_NAME}.account_id=${AccountsDao.TABLE_NAME}.id " +
                "INNER JOIN ${TokenDescriptionDao.TABLE_NAME} ON ${TransactionsDao.TABLE_NAME}.token_description_id=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE ${TransactionsDao.TABLE_NAME}.status='PENDING' " +
                "ORDER BY ${TransactionsDao.TABLE_NAME}.timestamp DESC"
    )
    abstract suspend fun getPendingTransactions(): List<YearnVaultInfo>

    companion object {
        const val TABLE_NAME: String = "yearn_history"
    }
}