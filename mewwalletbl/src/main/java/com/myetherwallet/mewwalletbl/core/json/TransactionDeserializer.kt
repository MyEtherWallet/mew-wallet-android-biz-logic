package com.myetherwallet.mewwalletbl.core.json

import com.google.gson.*
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.eip.eip155.LegacyTransaction
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import java.lang.reflect.Type
import java.math.BigInteger

/**
 * Created by BArtWell on 10.07.2020.
 */

class TransactionDeserializer : JsonDeserializer<Transaction> {

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Transaction? {
        val jsonObject = json?.asJsonObject
        val gasLimit = if (jsonObject?.has("gasLimit") == true) {
            BigIntegerSerializer.hexToBigInteger(json.asJsonObject?.get("gasLimit")!!)
        } else if (jsonObject?.has("gas") == true) {
            BigIntegerSerializer.hexToBigInteger(json.asJsonObject?.get("gas")!!)
        } else {
            BigInteger.ZERO
        }
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(BigInteger::class.java, BigIntegerSerializer())
            .registerTypeAdapter(ByteArray::class.java, ByteArraySerializer())
            .registerTypeAdapter(Address::class.java, AddressSerializer())
            .create()
        val transaction = gson.fromJson(json, LegacyTransaction::class.java)
        transaction.eipType = Transaction.EIPTransactionType.LEGACY
        transaction.gasLimit = gasLimit
        if (jsonObject?.has("nonce") != true || jsonObject.get("nonce")?.isJsonNull == true) {
            transaction.nonce = BigInteger.ZERO
        }
        if (jsonObject?.has("data") != true || jsonObject.get("data")?.isJsonNull == true) {
            transaction.data = byteArrayOf()
        }
        return transaction
    }
}
