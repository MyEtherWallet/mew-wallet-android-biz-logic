package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = "tokens_descriptions", indices = [Index(value = ["address"], unique = true)])
open class EntityTokenDescription(
    val address: Address,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}