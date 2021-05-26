package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.DappInfo
import com.myetherwallet.mewwalletbl.data.database.EntityBrowserFavorite

@Dao
abstract class BrowserFavoriteDao : BaseDao<EntityBrowserFavorite> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.url," +
                "CASE WHEN ${DappDao.TABLE_NAME}.name IS NULL THEN $TABLE_NAME.title ELSE ${DappDao.TABLE_NAME}.name END AS title," +
                "${DappDao.TABLE_NAME}.desc," +
                "${DappDao.TABLE_NAME}.img," +
                "${DappDao.TABLE_NAME}.category," +
                "$TABLE_NAME.timestamp," +
                "${DappDao.TABLE_NAME}.id AS dappId " +
                "FROM $TABLE_NAME LEFT JOIN ${DappDao.TABLE_NAME} ON $TABLE_NAME.url = ${DappDao.TABLE_NAME}.url ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract suspend fun getAll(): List<DappInfo>

    @Query("SELECT * FROM $TABLE_NAME WHERE url=:url")
    abstract suspend fun get(url: String): EntityBrowserFavorite?

    @Query("DELETE FROM $TABLE_NAME WHERE id=:id")
    abstract suspend fun delete(id: Long)

    companion object {
        const val TABLE_NAME: String = "browser_favorite"
    }
}