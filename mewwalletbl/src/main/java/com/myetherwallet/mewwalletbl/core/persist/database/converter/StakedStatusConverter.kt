package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.staked.StakedStatus

class StakedStatusConverter {
    @TypeConverter
    fun toStakedStatus(value: String?) = value?.let { StakedStatus.valueOf(it) }

    @TypeConverter
    fun toString(value: StakedStatus?) = value?.name
}
