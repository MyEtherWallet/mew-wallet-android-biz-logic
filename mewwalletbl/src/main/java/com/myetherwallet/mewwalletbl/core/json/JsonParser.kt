package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.myetherwallet.mewwalletbl.data.EncryptedMessage
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type
import java.math.BigDecimal
import java.math.BigInteger

/**
 * Created by BArtWell on 24.07.2019.
 */

object JsonParser {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(BigInteger::class.java, BigIntegerSerializer())
        .registerTypeAdapter(BigDecimal::class.java, BigDecimalSerializer())
        .registerTypeAdapter(ByteArray::class.java, ByteArraySerializer())
        .registerTypeAdapter(EncryptedMessage::class.java, EncryptedMessageSerializer())
        .registerTypeAdapter(Address::class.java, AddressSerializer())
        .registerTypeAdapter(Transaction::class.java, TransactionDeserializer())
        .create()

    fun <T> fromJson(json: ByteArray, c: Class<T>): T = fromJson(String(json), c)

    fun <T> fromJson(json: ByteArray, type: Type): T = fromJson(String(json), type)

    fun <T> fromJson(json: String, type: Type): T = gson.fromJson(json, type)

    fun <T> fromJson(json: String, c: Class<T>): T = gson.fromJson(json, c)

    fun <T> fromJson(json: JsonElement, c: Class<T>): T = gson.fromJson(json, c)

    fun <T> fromJson(json: JSONObject, c: Class<T>): T = gson.fromJson(json.toString(), c)

    fun <T> toJson(data: T): String = gson.toJson(data)

    fun <T> toJsonObject(data: T) = JSONObject(toJson(data))

    fun isObject(json: String): Boolean = try {
        JSONObject(json)
        true
    } catch (e: JSONException) {
        false
    }

    fun isArray(json: String): Boolean = try {
        JSONArray(json)
        true
    } catch (e: JSONException) {
        false
    }
}
