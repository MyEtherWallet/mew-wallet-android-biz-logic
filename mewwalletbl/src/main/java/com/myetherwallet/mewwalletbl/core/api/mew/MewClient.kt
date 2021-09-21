package com.myetherwallet.mewwalletbl.core.api.mew

import com.google.gson.GsonBuilder
import com.myetherwallet.mewwalletbl.MewEnvironment
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.BaseClient
import com.myetherwallet.mewwalletbl.core.json.*
import com.myetherwallet.mewwalletbl.data.api.binance.MoveStatus
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 21.09.2019.
 */

private const val TAG = "MewClient"

class MewClient : BaseClient() {

    override val retrofit = createRetrofit()
    var awsLimitListener: (() -> Unit)? = null

    private fun createRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .registerTypeAdapterFactory(NullableTypeAdapterFactory())
            // "2019-11-08T19:41:17.000Z"
            .registerTypeAdapter(Date::class.java, DateDeserializer())
            .registerTypeAdapter(Address::class.java, AddressSerializer())
            .registerTypeAdapter(BigDecimal::class.java, BigDecimalSerializer())
            .registerTypeAdapter(BigInteger::class.java, BigIntegerSerializer())
            .registerTypeAdapter(MoveStatus::class.java, MoveStatusSerializer())
            .create()

        return Retrofit.Builder()
            .baseUrl(MewEnvironment.current.api)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    override fun setupClient(): OkHttpClient.Builder {
        val builder = super.setupClient()
        builder.addInterceptor(CatchAwsLimitErrorInterceptor())
        builder.addInterceptor(RetryInterceptor())
        return builder
    }

    override fun getApiName() = NAME

    companion object {
        const val NAME = "Mew"
    }

    private inner class CatchAwsLimitErrorInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            if (response.code == 429) {
                awsLimitListener?.invoke()
            }
            return response
        }
    }

    private class RetryInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            var response = chain.proceed(request)
            var attempt = 1
            while (!response.isSuccessful && hasErrorHeader(response.headers) && attempt <= 3) {
                MewLog.w(TAG, "Retry request (attempt $attempt)")
                attempt++
                response = chain.proceed(request)
            }
            return response
        }

        private fun hasErrorHeader(headers: Headers) = headers.find {
            it.first.equals("x-cache", true) &&
                    it.first.equals("Error from cloudfront", true)
        } != null
    }
}
