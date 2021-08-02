package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

/**
 * Created by BArtWell on 23.06.2021.
 */

private val exclude = arrayOf("currency", "exchangeRate")

class NullableTypeAdapterFactory : TypeAdapterFactory {

    override fun <T : Any> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val delegate = gson.getDelegateAdapter(this, type)
        // If the class isn't kotlin, don't use the custom type adapter
        if (type.rawType.declaredAnnotations.none { it.annotationClass.qualifiedName == "kotlin.Metadata" }) {
            return null
        }
        return object : TypeAdapter<T>() {

            override fun write(out: JsonWriter, value: T?) = delegate.write(out, value)

            override fun read(input: JsonReader): T? {
                val value: T? = delegate.read(input)
                if (value != null) {
                    val kotlinClass = type.rawType.kotlin
                    kotlinClass.memberProperties.forEach {
                        if (!it.returnType.isMarkedNullable && it.getter.call(value) == null && !exclude.contains(it.name)) {
                            throw JsonParseException("Value of non-nullable member ${it.name} in ${kotlinClass.simpleName} cannot be null")
                        }
                    }
                }
                return value
            }
        }
    }
}
