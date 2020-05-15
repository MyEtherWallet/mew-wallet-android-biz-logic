package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by BArtWell on 11.11.2019.
 */

class DateDeserializer : JsonDeserializer<Date> {

    private var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)

    init {
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        json?.asString?.let {
            try {
                return dateFormat.parse(it)
            } catch (e: Exception) {
            }
        }
        return null
    }
}
