package com.myetherwallet.mewwalletbl.core.api.mew

import com.google.gson.GsonBuilder
import com.myetherwallet.mewwalletbl.NetworkConfig
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Client
import com.myetherwallet.mewwalletbl.core.json.AddressSerializer
import com.myetherwallet.mewwalletbl.core.json.DateDeserializer
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

/**
 * Created by BArtWell on 21.09.2019.
 */

class MewClient : Client {

    override val retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        val gson = GsonBuilder()
            // "2019-11-08T19:41:17.000Z"
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(Address::class.java, AddressSerializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(NetworkConfig.current.api)
            .client(createClient())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private fun createClient(): OkHttpClient {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (MewLog.shouldDisplayLogs()) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpClientBuilder.build()
    }
}
