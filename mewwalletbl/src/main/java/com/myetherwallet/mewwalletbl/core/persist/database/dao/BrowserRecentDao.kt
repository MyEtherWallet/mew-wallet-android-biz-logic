package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.database.DappInfo
import com.myetherwallet.mewwalletbl.data.database.EntityBrowserRecent

@Dao
abstract class BrowserRecentDao : BaseDao<EntityBrowserRecent> {

    @Query(
        "SELECT * FROM $TABLE_NAME ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract suspend fun get(): List<EntityBrowserRecent>

    @Query(
        "SELECT " +
                "$TABLE_NAME.url," +
                "CASE WHEN ${DappDao.TABLE_NAME}.name IS NULL THEN $TABLE_NAME.title ELSE ${DappDao.TABLE_NAME}.name END AS title," +
                "${DappDao.TABLE_NAME}.desc," +
                "${DappDao.TABLE_NAME}.img," +
                "${DappDao.TABLE_NAME}.category," +
                "$TABLE_NAME.timestamp," +
                "${DappDao.TABLE_NAME}.id AS dappId " +
                "FROM $TABLE_NAME LEFT JOIN ${DappDao.TABLE_NAME} ON $TABLE_NAME.url = ${DappDao.TABLE_NAME}.url " +
                "WHERE $TABLE_NAME.blockchain=:blockchain " +
                "ORDER BY $TABLE_NAME.timestamp DESC LIMIT 15"
    )
    abstract suspend fun getAll(blockchain: Blockchain): List<DappInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE url=:url")
    abstract suspend fun get(url: String): EntityBrowserRecent?

    @Query("DELETE FROM $TABLE_NAME WHERE id=:id")
    abstract suspend fun delete(id: Long)

    @Query("DELETE FROM $TABLE_NAME WHERE $TABLE_NAME.blockchain=:blockchain")
    abstract suspend fun clear(blockchain: Blockchain)

    companion object {
        const val TABLE_NAME: String = "browser_recent"
    }
}