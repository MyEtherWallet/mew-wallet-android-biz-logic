package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityLidoHistory
import com.myetherwallet.mewwalletbl.data.database.TransactionInfo

@Dao
abstract class LidoHistoryDao : BaseDao<EntityLidoHistory> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityLidoHistory>

    @Query(
        "SELECT " +
                "${TransactionsDao.TABLE_NAME}.tx_hash AS hash," +
                "(SELECT ${AccountsDao.TABLE_NAME}.address FROM ${AccountsDao.TABLE_NAME} WHERE ${AccountsDao.TABLE_NAME}.id=${TransactionsDao.TABLE_NAME}.account_id) AS address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=${TransactionsDao.TABLE_NAME}.from_recipient_id)  AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=${TransactionsDao.TABLE_NAME}.to_recipient_id)  AS toRecipient," +
                "amount," +
                "status," +
                "${TransactionsDao.TABLE_NAME}.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${PricesDao.TABLE_NAME}.price," +
                "${TransactionsDao.TABLE_NAME}.nonce," +
                "${TransactionsDao.TABLE_NAME}.blockchain " +

                "FROM $TABLE_NAME INNER JOIN ${TransactionsDao.TABLE_NAME} ON $TABLE_NAME.tx_hash=${TransactionsDao.TABLE_NAME}.tx_hash "+
                "INNER JOIN ${AccountsDao.TABLE_NAME} ON ${TransactionsDao.TABLE_NAME}.account_id=${AccountsDao.TABLE_NAME}.id " +
                "INNER JOIN ${TokenDescriptionDao.TABLE_NAME} ON ${TransactionsDao.TABLE_NAME}.token_description_id=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE ${TransactionsDao.TABLE_NAME}.status='PENDING' " +
                "ORDER BY ${TransactionsDao.TABLE_NAME}.timestamp DESC"
    )
    abstract suspend fun getPendingTransactions(): List<TransactionInfo>

    @Query("UPDATE $TABLE_NAME SET tx_hash=:newTxHash WHERE tx_hash=:oldTxHash")
    abstract suspend fun updateTxHash(oldTxHash: String, newTxHash: String)

    companion object {
        const val TABLE_NAME: String = "lido_history"
    }
}