package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletbl.data.api.TransactionStatus

/**
 * Created by BArtWell on 24.10.2019.
 */

class TransactionStatusTypeConverter {

    @TypeConverter
    fun toTransactionStatus(value: String?) = value?.let { TransactionStatus.valueOf(it) }

    @TypeConverter
    fun toString(value: TransactionStatus?) = value?.name
}
