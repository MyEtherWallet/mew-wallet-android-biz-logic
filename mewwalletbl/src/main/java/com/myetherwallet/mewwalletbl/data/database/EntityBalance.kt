package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.BalancesDao
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = BalancesDao.TABLE_NAME)
data class EntityBalance(
    val tokenId: Long,
    val amount: BigInteger,
    val timestamp: Date
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}