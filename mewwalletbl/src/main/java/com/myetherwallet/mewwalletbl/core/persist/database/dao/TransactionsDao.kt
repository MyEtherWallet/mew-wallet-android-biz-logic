package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus
import com.myetherwallet.mewwalletbl.data.database.EntityTransaction
import com.myetherwallet.mewwalletbl.data.database.TransactionInfo
import java.util.*

@Dao
abstract class TransactionsDao : BaseDao<EntityTransaction> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.tx_hash AS hash," +
                "${AccountsDao.TABLE_NAME}.address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.from_recipient_id) AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.to_recipient_id) AS toRecipient," +
                "amount," +
                "status," +
                "$TABLE_NAME.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${PricesDao.TABLE_NAME}.price," +
                "$TABLE_NAME.nonce," +
                "$TABLE_NAME.blockchain " +
                "FROM $TABLE_NAME INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.account_id=${AccountsDao.TABLE_NAME}.id " +
                "AND $TABLE_NAME.token_description_id=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} " +
                "ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE $TABLE_NAME.account_id=:accountId AND $TABLE_NAME.blockchain=:blockchain " +
                "ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract suspend fun getTransactions(accountId: Long, blockchain: Blockchain): List<TransactionInfo>

    @Query(
        "SELECT " +
                "$TABLE_NAME.tx_hash AS hash," +
                "${AccountsDao.TABLE_NAME}.address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.from_recipient_id)  AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.to_recipient_id)  AS toRecipient," +
                "amount," +
                "status," +
                "$TABLE_NAME.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${PricesDao.TABLE_NAME}.price," +
                "${TABLE_NAME}.nonce," +
                "$TABLE_NAME.blockchain " +
                "FROM $TABLE_NAME INNER JOIN ${AccountsDao.TABLE_NAME} INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.account_id=${AccountsDao.TABLE_NAME}.id " +
                "AND $TABLE_NAME.token_description_id=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} " +
                "ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE NOT(${AccountsDao.TABLE_NAME}.hide) AND $TABLE_NAME.blockchain=:blockchain " +
                "ORDER BY $TABLE_NAME.timestamp DESC,(CASE WHEN ${AccountsDao.TABLE_NAME}.address = toRecipient THEN 1 ELSE 0 END) DESC LIMIT 3"
    )
    abstract suspend fun getLastTransactions(blockchain: Blockchain): List<TransactionInfo>

    @Query(
        "SELECT " +
                "$TABLE_NAME.tx_hash AS hash," +
                "(SELECT ${AccountsDao.TABLE_NAME}.address FROM ${AccountsDao.TABLE_NAME} WHERE ${AccountsDao.TABLE_NAME}.id=$TABLE_NAME.account_id) AS address," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.from_recipient_id)  AS fromRecipient," +
                "(SELECT ${RecipientDao.TABLE_NAME}.address FROM ${RecipientDao.TABLE_NAME} WHERE ${RecipientDao.TABLE_NAME}.id=$TABLE_NAME.to_recipient_id)  AS toRecipient," +
                "amount," +
                "status," +
                "$TABLE_NAME.timestamp," +
                "${TokenDescriptionDao.TABLE_NAME}.name AS tokenName," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo," +
                "${TokenDescriptionDao.TABLE_NAME}.address AS contract," +
                "${PricesDao.TABLE_NAME}.price," +
                "$TABLE_NAME.blockchain " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.token_description_id=${TokenDescriptionDao.TABLE_NAME}.id " +
                "LEFT JOIN ${PricesDao.TABLE_NAME} " +
                "ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE $TABLE_NAME.tx_hash=:hash"
    )
    abstract suspend fun getTransaction(hash: String): TransactionInfo?

    @Query("UPDATE $TABLE_NAME SET status=:status, timestamp=:timestamp WHERE tx_hash=:txHash")
    abstract suspend fun updateStatus(txHash: String, status: TransactionStatus, timestamp: Date)

    @Query("DELETE FROM $TABLE_NAME WHERE tx_hash=:txHash")
    abstract suspend fun deleteTransaction(txHash: String)

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityTransaction>

    companion object {
        const val TABLE_NAME: String = "transactions"
    }
}