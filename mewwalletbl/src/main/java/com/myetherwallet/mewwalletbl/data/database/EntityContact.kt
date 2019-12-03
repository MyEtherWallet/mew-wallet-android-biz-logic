package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = "contacts")
data class EntityContact(
    val walletId:Long,
    val name: String,
    val address: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}