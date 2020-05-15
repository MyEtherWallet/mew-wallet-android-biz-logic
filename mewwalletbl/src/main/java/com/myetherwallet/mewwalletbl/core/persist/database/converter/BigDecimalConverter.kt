package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import java.math.BigDecimal

class BigDecimalConverter {

    @TypeConverter
    fun toBigDecimal(value: Double?) = value?.let { BigDecimal.valueOf(it) }

    @TypeConverter
    fun toDouble(value: BigDecimal?) = value?.toDouble()
}