package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.AccountsDao
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = AccountsDao.TABLE_NAME)
data class EntityAccount(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val position: Long,
    val walletId: Long,
    val address: Address,
    val name: String,
    val hide: Boolean,
    @ColumnInfo(name = "anonymous_id")
    val anonymousId: String,
    var nonce: String,
    @ColumnInfo(name = "eth2_address")
    val eth2Address: String
) {

    fun getIndex() = (id - 1).toInt()
}
