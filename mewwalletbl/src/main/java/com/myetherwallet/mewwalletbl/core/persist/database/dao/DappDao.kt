package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.DappInfo
import com.myetherwallet.mewwalletbl.data.database.EntityDapp

@Dao
abstract class DappDao : BaseDao<EntityDapp> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.url, " +
                "$TABLE_NAME.name AS title, " +
                "$TABLE_NAME.desc, " +
                "$TABLE_NAME.img, " +
                "$TABLE_NAME.category, " +
                "date('now') AS timestamp, " +
                "$TABLE_NAME.id as dappId " +
                "FROM $TABLE_NAME ORDER BY id ASC"
    )
    abstract suspend fun getAll(): List<DappInfo>

    companion object {
        const val TABLE_NAME: String = "dapp"
    }
}