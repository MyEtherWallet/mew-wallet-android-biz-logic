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

    private val formatStrings = listOf("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss")

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Date? {
        json?.asString?.let { value ->
            formatStrings.forEach { pattern ->
                with(SimpleDateFormat(pattern, Locale.US)) {
                    try {
                        timeZone = TimeZone.getTimeZone("UTC")
                        return parse(value)
                    } catch (e: Exception) {
                    }
                }
            }
        }
        return null
    }
}
