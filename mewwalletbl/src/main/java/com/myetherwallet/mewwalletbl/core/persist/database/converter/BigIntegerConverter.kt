package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import java.math.BigInteger

/**
 * Created by BArtWell on 22.11.2019.
 */

class BigIntegerConverter {

    @TypeConverter
    fun toBigInteger(value: String?) = value?.hexToBigInteger()

    @TypeConverter
    fun toString(value: BigInteger?) = value?.toHexString()
}
