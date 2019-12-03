package com.myetherwallet.mewwalletbl.data

import com.myetherwallet.mewwalletbl.core.json.JsonParser

/**
 * Created by BArtWell on 24.07.2019.
 */

open class BaseMessage {
    fun toByteArray() = JsonParser.toJson(this).toByteArray()
}