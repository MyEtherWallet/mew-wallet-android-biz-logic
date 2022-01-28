package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.myetherwallet.mewwalletbl.data.api.binance.MoveStatus
import java.lang.reflect.Type
import java.util.*

class MoveStatusSerializer : JsonDeserializer<MoveStatus> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): MoveStatus {
        return MoveStatus.valueOf(json.asString.uppercase(Locale.ENGLISH))
    }
}