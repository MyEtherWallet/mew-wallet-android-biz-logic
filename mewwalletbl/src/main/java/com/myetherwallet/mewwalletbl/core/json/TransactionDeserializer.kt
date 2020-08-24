package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.*
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import java.lang.reflect.Type
import java.math.BigInteger

/**
 * Created by BArtWell on 10.07.2020.
 */

class TransactionDeserializer : JsonDeserializer<Transaction> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Transaction? {
        val gasLimit = if (json?.asJsonObject?.has("gasLimit") == true) {
            json.asJsonObject?.get("gasLimit")?.asString?.hexToBigInteger()!!
        } else if (json?.asJsonObject?.has("gas") == true) {
            json.asJsonObject?.get("gas")?.asString?.hexToBigInteger()!!
        } else {
            BigInteger.ZERO
        }
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(BigInteger::class.java, BigIntegerSerializer())
            .registerTypeAdapter(ByteArray::class.java, ByteArraySerializer())
            .registerTypeAdapter(Address::class.java, AddressSerializer())
            .create()
        val transaction = gson.fromJson(json, Transaction::class.java)
        transaction.gasLimit = gasLimit
        return transaction
    }
}
