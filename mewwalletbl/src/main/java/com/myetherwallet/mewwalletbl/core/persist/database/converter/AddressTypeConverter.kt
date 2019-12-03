package com.myetherwallet.mewwalletbl.core.persist.database.converter

import androidx.room.TypeConverter
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 24.10.2019.
 */

class AddressTypeConverter {

    @TypeConverter
    fun toAddress(value: String?) = value?.let { Address.createRaw(it) }

    @TypeConverter
    fun toString(value: Address?) = value?.address
}
