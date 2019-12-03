package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.*
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.lang.reflect.Type

/**
 * Created by BArtWell on 27.07.2019.
 */

private const val ADDRESS_FIELD_NAME = "address"

class AddressSerializer : JsonDeserializer<Address>, JsonSerializer<Address> {

    override fun serialize(src: Address?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        src?.let {
            val jsonObject = JsonObject()
            jsonObject.addProperty(ADDRESS_FIELD_NAME, it.address)
            return jsonObject
        }
        return null
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Address {
        return if ((json as JsonPrimitive).isString) {
            Address.createRaw(json.asString)
        } else {
            val jsonObject = json.asJsonObject
            Address.createRaw(jsonObject.get("").asString)
        }
    }
}
