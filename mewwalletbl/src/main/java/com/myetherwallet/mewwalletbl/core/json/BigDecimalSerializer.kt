package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by BArtWell on 23.03.2021.
 */

class BigDecimalSerializer : JsonDeserializer<BigDecimal> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BigDecimal {
        return hexToBigDecimal(json)
    }

    companion object {
        fun hexToBigDecimal(json: JsonElement): BigDecimal {
            val string = json.asString
            return if (string.isNullOrEmpty() || string == "NaN") {
                BigDecimal.ZERO
            } else {
                string.toBigDecimal()
            }
        }
    }
}
