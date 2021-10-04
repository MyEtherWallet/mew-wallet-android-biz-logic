package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnType

class YearnTypeConverter  {

    @TypeConverter
    fun toYearnType(value: String?) = value?.let { YearnType.valueOf(it) }

    @TypeConverter
    fun toString(value: YearnType?) = value?.name
}