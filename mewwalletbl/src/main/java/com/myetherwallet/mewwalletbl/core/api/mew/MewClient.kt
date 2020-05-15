package com.myetherwallet.mewwalletbl.core.api.mew

import com.google.gson.GsonBuilder
import com.myetherwallet.mewwalletbl.MewEnvironment
import com.myetherwallet.mewwalletbl.core.api.BaseClient
import com.myetherwallet.mewwalletbl.core.json.AddressSerializer
import com.myetherwallet.mewwalletbl.core.json.BigIntegerSerializer
import com.myetherwallet.mewwalletbl.core.json.DateDeserializer
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 21.09.2019.
 */

class MewClient : BaseClient() {

    override val retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        val gson = GsonBuilder()
            // "2019-11-08T19:41:17.000Z"
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(Address::class.java, AddressSerializer())
            .registerTypeAdapter(BigInteger::class.java, BigIntegerSerializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(MewEnvironment.current.api)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    override fun getApiName() = NAME

    companion object {
        const val NAME = "Mew"
    }
}
