package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.database.SwapTokenCategory

class SwapTokenCategoryConverter {
    @TypeConverter
    fun toSwapTokenCategory(value: String?) = value?.let { SwapTokenCategory.valueOf(it) }

    @TypeConverter
    fun toString(value: SwapTokenCategory?) = value?.name
}