package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import com.myetherwallet.mewwalletbl.data.database.EntityTransaction
import com.myetherwallet.mewwalletbl.data.database.Recent
import com.myetherwallet.mewwalletbl.data.database.TransactionInfo
import java.util.*

@Dao
abstract class TransactionsDao : BaseDao<EntityTransaction> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.txHash AS hash," +
                "${AccountsDao.TABLE_NAME}.address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.fromRecipientId) AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.toRecipientId) AS toRecipient," +
                "amount," +
                "status," +
                "$TABLE_NAME.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${PricesDao.TABLE_NAME}.price " +
                "FROM $TABLE_NAME INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} INNER JOIN ${PricesDao.TABLE_NAME} " +
                "ON $TABLE_NAME.accountId=${AccountsDao.TABLE_NAME}.id " +
                "AND $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE $TABLE_NAME.accountId=:accountId " +
                "ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract fun getTransactions(accountId: Long): List<TransactionInfo>

    @Query(
        "SELECT " +
                "$TABLE_NAME.txHash AS hash," +
                "${AccountsDao.TABLE_NAME}.address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.fromRecipientId)  AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.toRecipientId)  AS toRecipient," +
                "amount," +
                "status," +
                "$TABLE_NAME.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${PricesDao.TABLE_NAME}.price " +
                "FROM $TABLE_NAME INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} INNER JOIN ${PricesDao.TABLE_NAME} " +
                "ON $TABLE_NAME.accountId=${AccountsDao.TABLE_NAME}.id " +
                "AND $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE NOT(${AccountsDao.TABLE_NAME}.hide) " +
                "ORDER BY $TABLE_NAME.timestamp DESC,(CASE WHEN ${AccountsDao.TABLE_NAME}.address = toRecipient THEN 1 ELSE 0 END) DESC LIMIT 3"
    )
    abstract fun getLastTransactions(): List<TransactionInfo>

    @Query(
        "SELECT " +
                "$TABLE_NAME.txHash AS hash," +
                "(SELECT ${AccountsDao.TABLE_NAME}.address FROM ${AccountsDao.TABLE_NAME} WHERE ${AccountsDao.TABLE_NAME}.id=$TABLE_NAME.accountId) AS address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.fromRecipientId)  AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.toRecipientId)  AS toRecipient," +
                "amount," +
                "status," +
                "$TABLE_NAME.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${PricesDao.TABLE_NAME}.price " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} INNER JOIN ${PricesDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE $TABLE_NAME.txHash=:hash"
    )
    abstract fun getTransaction(hash: String): TransactionInfo?

    @Query("UPDATE $TABLE_NAME SET status=:status, timestamp=:timestamp WHERE txHash=:txHash")
    abstract fun updateStatus(txHash: String, status: TransactionStatus, timestamp: Date)

    @Query("DELETE FROM $TABLE_NAME WHERE txHash=:txHash")
    abstract fun deleteTransaction(txHash: String)

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAll(): List<EntityTransaction>

    companion object {
        const val TABLE_NAME: String = "transactions"
    }
}