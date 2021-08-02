package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.api.binance.MoveStatus
import com.myetherwallet.mewwalletbl.data.staked.StakedStatus

class MoveStatusConverter {
    @TypeConverter
    fun toMoveStatus(value: String?) = value?.let { MoveStatus.valueOf(it) }

    @TypeConverter
    fun toString(value: MoveStatus?) = value?.name
}
