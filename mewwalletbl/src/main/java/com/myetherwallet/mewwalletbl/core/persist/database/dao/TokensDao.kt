package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityToken
import com.myetherwallet.mewwalletbl.data.database.ExtToken
import com.myetherwallet.mewwalletbl.data.database.TokensCount
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class TokensDao : BaseDao<EntityToken> {

    @Query(
        "SELECT * FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id"
    )
    abstract fun getAll(): List<ExtToken>

    @Query(
        "SELECT * FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE accountId=:account"
    )
    abstract fun getAssets(account: Long): List<ExtToken>

    @Query("SELECT * FROM $TABLE_NAME WHERE isPrimary=1 AND accountId=:account")
    abstract fun getPrimary(account: Long): EntityToken?

    @Query(
        "SELECT $TABLE_NAME.id FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE $TABLE_NAME.accountId=:accountId AND ${TokenDescriptionDao.TABLE_NAME}.address=:contract"
    )
    abstract fun getTokenId(accountId: Long, contract: Address): Long

    @Query(
        "SELECT ${TokenDescriptionDao.TABLE_NAME}.address " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} INNER JOIN ${AccountsDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND $TABLE_NAME.accountId=${AccountsDao.TABLE_NAME}.id " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address"
    )
    abstract fun getContracts(address: Address): List<Address>

    @Query(
        "SELECT ${AccountsDao.TABLE_NAME}.address,COUNT($TABLE_NAME.id) AS count " +
                "FROM $TABLE_NAME,${AccountsDao.TABLE_NAME} " +
                "WHERE $TABLE_NAME.accountId=${AccountsDao.TABLE_NAME}.id AND $TABLE_NAME.isPrimary=0 " +
                "GROUP BY ${AccountsDao.TABLE_NAME}.address"
    )
    abstract fun getTokensCount(): List<TokensCount>

    @Query("DELETE FROM $TABLE_NAME WHERE id=:id")
    abstract fun removeToken(id: Long)

    companion object {
        const val TABLE_NAME: String = "tokens"
    }
}
