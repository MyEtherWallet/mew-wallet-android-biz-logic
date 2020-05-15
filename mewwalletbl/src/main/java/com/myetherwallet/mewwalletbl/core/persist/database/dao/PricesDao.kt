package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityPrice
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class PricesDao : BaseDao<EntityPrice> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract fun getAll(): List<EntityPrice>

    @Query(
        "SELECT $TABLE_NAME.price " +
                "FROM $TABLE_NAME, ${AccountsDao.TABLE_NAME}, ${TokensDao.TABLE_NAME}, ${TokenDescriptionDao.TABLE_NAME} " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address AND " +
                "${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId AND " +
                "${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id AND " +
                "${TokensDao.TABLE_NAME}.isPrimary=1 AND " +
                "$TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
    )
    abstract fun getLastPrimary(address: Address): Double

    @Query(
        "SELECT $TABLE_NAME.price " +
                "FROM $TABLE_NAME, ${AccountsDao.TABLE_NAME}, ${TokensDao.TABLE_NAME}, ${TokenDescriptionDao.TABLE_NAME} " +
                "WHERE ${AccountsDao.TABLE_NAME}.address=:address " +
                "AND ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
                "AND ${TokenDescriptionDao.TABLE_NAME}.address=:contractAddress " +
                "AND $TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
    )
    abstract fun getLast(address: Address, contractAddress: Address): Double

    @Query(
        "SELECT $TABLE_NAME.price " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE ${TokenDescriptionDao.TABLE_NAME}.address=:contractAddress " +
                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
    )
    abstract fun getLastPrice(contractAddress: Address): Double

    @Query(
        "SELECT $TABLE_NAME.price " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE ${TokenDescriptionDao.TABLE_NAME}.address=:contractAddress " +
                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
    )
    abstract fun getLast(contractAddress: Address): Double

    companion object {
        const val TABLE_NAME: String = "prices"
    }
}
