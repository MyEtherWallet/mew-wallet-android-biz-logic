package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.*
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.core.extension.isHex
import java.lang.reflect.Type

/**
 * Created by BArtWell on 24.07.2019.
 */

class ByteArraySerializer : JsonSerializer<ByteArray>, JsonDeserializer<ByteArray> {
    override fun serialize(src: ByteArray, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        val jsonArray = JsonArray()
        for (byte in src) {
            jsonArray.add(byteToInt(byte))
        }
        return jsonArray
    }

    private fun byteToInt(byte: Byte): Int {
        return byte.toInt() and 0xff
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ByteArray {
        if (json is JsonArray) {
            val byteArray = ByteArray(json.size())
            for (i in 0 until json.size()) {
                byteArray[i] = (json.get(i).asInt).toByte()
            }
            return byteArray
        } else if (json is JsonPrimitive && json.isString) {
            val string = json.asString
            if (string.isHex()) {
                return string.hexToByteArray()
            }
        }
        return ByteArray(0)
    }
}
