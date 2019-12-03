package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import java.lang.reflect.Type
import java.math.BigInteger

/**
 * Created by BArtWell on 24.07.2019.
 */

class BigIntegerSerializer : JsonDeserializer<BigInteger> {

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): BigInteger {
        return json.asString.hexToBigInteger()
    }
}
