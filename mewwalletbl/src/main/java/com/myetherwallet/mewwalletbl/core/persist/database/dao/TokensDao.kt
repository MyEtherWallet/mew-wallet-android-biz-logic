package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.EntityToken
import com.myetherwallet.mewwalletbl.data.database.EntityTokenDescription
import com.myetherwallet.mewwalletbl.data.database.ExtToken
import com.myetherwallet.mewwalletbl.data.database.TokensCount
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class TokensDao : BaseDao<EntityToken> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityToken>

    @Query(
        "SELECT $TABLE_NAME.id," +
                "$TABLE_NAME.accountId," +
                "$TABLE_NAME.tokenDescriptionId," +
                "$TABLE_NAME.isPrimary," +
                "${TokenDescriptionDao.TABLE_NAME}.address, " +
                "${TokenDescriptionDao.TABLE_NAME}.decimals, " +
                "${TokenDescriptionDao.TABLE_NAME}.name, " +
                "${TokenDescriptionDao.TABLE_NAME}.symbol, " +
                "${TokenDescriptionDao.TABLE_NAME}.logo " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE accountId=:account AND ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain"
    )
    abstract suspend fun getAssets(blockchain: Blockchain, account: Long): List<ExtToken>

    @Query(
        "SELECT ${TokenDescriptionDao.TABLE_NAME}.address " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND $TABLE_NAME.accountId=${AccountsDao.TABLE_NAME}.id " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address AND ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain"
    )
    abstract suspend fun getContracts(blockchain: Blockchain, address: Address): List<Address>

    @Query("DELETE FROM $TABLE_NAME WHERE id=:id")
    abstract suspend fun removeToken(id: Long)

    @Query("UPDATE $TABLE_NAME SET isPrimary=:isPrimary WHERE accountId=:accountId AND tokenDescriptionId=:descriptionId")
    abstract suspend fun updatePrimary(accountId: Long, descriptionId: Long, isPrimary: Boolean)

    @Query("UPDATE $TABLE_NAME SET isHidden=:isHidden WHERE tokenDescriptionId=:descriptionId")
    abstract suspend fun updateHidden(descriptionId: Long, isHidden: Boolean)

    @Query("SELECT id FROM $TABLE_NAME WHERE accountId=:accountId AND tokenDescriptionId=:descriptionId")
    abstract suspend fun get(accountId: Long, descriptionId: Long): Long

    suspend fun getExistsIdOrInsert(entity: EntityToken): Long {
        val id = Database.instance.getTokensDao().insertOrIgnore(entity)
        return if (id == -1L) {
            Database.instance.getTokensDao().get(entity.accountId, entity.tokenDescriptionId)
        } else {
            id
        }
    }

    companion object {
        const val TABLE_NAME: String = "tokens"
    }
}
