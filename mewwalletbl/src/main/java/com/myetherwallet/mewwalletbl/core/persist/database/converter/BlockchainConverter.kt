package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.Blockchain

class BlockchainConverter {
    @TypeConverter
    fun toBlockchain(value: String?) = value?.let { Blockchain.valueOf(it) }

    @TypeConverter
    fun toString(value: Blockchain?) = value?.name
}