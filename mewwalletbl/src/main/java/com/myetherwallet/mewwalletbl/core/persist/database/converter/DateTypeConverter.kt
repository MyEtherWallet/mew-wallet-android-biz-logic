package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.*
import java.util.*

/**
 * Created by BArtWell on 19.09.2019.
 */

class DateTypeConverter {

    @TypeConverter
    fun toDate(value: Long?) = value?.let { Date(it) }

    @TypeConverter
    fun toLong(value: Date?) = value?.time
}
