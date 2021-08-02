package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.EntityPrice
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class PricesDao : BaseDao<EntityPrice> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll(): List<EntityPrice>

//    @Query(
//        "SELECT $TABLE_NAME.price " +
//                "FROM $TABLE_NAME, ${AccountsDao.TABLE_NAME}, ${TokensDao.TABLE_NAME}, ${TokenDescriptionDao.TABLE_NAME} " +
//                "WHERE ${AccountsDao.TABLE_NAME}.address=:address AND " +
//                "${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId AND " +
//                "${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id AND " +
//                "${TokensDao.TABLE_NAME}.isPrimary=1 AND " +
//                "$TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id AND " +
//                "${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain " +
//                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
//    )
//    abstract suspend fun getLastPrimary(blockchain: Blockchain, address: Address): Double

//    @Query(
//        "SELECT $TABLE_NAME.price " +
//                "FROM $TABLE_NAME, ${AccountsDao.TABLE_NAME}, ${TokensDao.TABLE_NAME}, ${TokenDescriptionDao.TABLE_NAME} " +
//                "WHERE ${AccountsDao.TABLE_NAME}.address=:address " +
//                "AND ${AccountsDao.TABLE_NAME}.id=${TokensDao.TABLE_NAME}.accountId " +
//                "AND ${TokenDescriptionDao.TABLE_NAME}.address=:contractAddress " +
//                "AND $TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
//                "AND ${TokensDao.TABLE_NAME}.tokenDescriptionId=${TokenDescriptionDao.TABLE_NAME}.id " +
//                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
//    )
//    abstract suspend fun getLast(address: Address, contractAddress: Address): Double

    @Query(
        "SELECT $TABLE_NAME.price " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON $TABLE_NAME.tokenId=${TokenDescriptionDao.TABLE_NAME}.id " +
                "WHERE ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain AND ${TokenDescriptionDao.TABLE_NAME}.address=:contractAddress " +
                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 1"
    )
    abstract suspend fun getLast(blockchain: Blockchain, contractAddress: Address): Double?

    @Query("SELECT * FROM $TABLE_NAME WHERE $TABLE_NAME.tokenId=:descriptionId")
    abstract suspend fun get(descriptionId: Long): EntityPrice?

    companion object {
        const val TABLE_NAME: String = "prices"
    }
}
