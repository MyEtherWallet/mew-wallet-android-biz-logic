package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityPurchase
import com.myetherwallet.mewwalletbl.data.database.Purchase

@Dao
abstract class PurchaseDao : BaseDao<EntityPurchase> {

    @Query(
        "SELECT " +
                "$TABLE_NAME.transactionId," +
                "$TABLE_NAME.fiatAmount," +
                "$TABLE_NAME.fiatCurrency," +
                "$TABLE_NAME.cryptoAmount," +
                "${TokenDescriptionDao.TABLE_NAME}.symbol AS cryptoSymbol," +
                "${TokenDescriptionDao.TABLE_NAME}.logo AS cryptoLogo," +
                "$TABLE_NAME.timestamp," +
                "$TABLE_NAME.status," +
                "$TABLE_NAME.provider " +
                "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
                "ON ${TokenDescriptionDao.TABLE_NAME}.id=$TABLE_NAME.cryptoDescriptionId " +
                "ORDER BY $TABLE_NAME.timestamp DESC"
    )
    abstract fun getAll(): List<Purchase>

    companion object {
        const val TABLE_NAME: String = "purchase"
    }
}