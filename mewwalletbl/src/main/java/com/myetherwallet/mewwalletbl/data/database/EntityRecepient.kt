package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = "recipients", indices = [Index(value = ["address"], unique = true)])
data class EntityRecipient(
    val address: Address
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}