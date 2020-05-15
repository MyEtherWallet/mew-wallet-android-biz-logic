package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.PurchaseState


class PurchaseStateTypeConverter {

    @TypeConverter
    fun toPurchaseState(value: String?) = value?.let { PurchaseState.valueOf(it) }

    @TypeConverter
    fun toString(value: PurchaseState?) = value?.name
}