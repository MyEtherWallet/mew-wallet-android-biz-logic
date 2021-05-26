package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.preference.Preferences
import okhttp3.Dispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by BArtWell on 10.04.2020.
 */

private const val DEFAULT_MAX_REQUESTS = 100
private const val DEFAULT_MAX_REQUESTS_PER_HOST = 25

abstract class BaseClient : Client {

    val client by lazy { createClient() }

    private fun createClient() = setupClient().build()

    open fun setupClient(): OkHttpClient.Builder {
        val okHttpClientBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (MewLog.shouldDisplayLogs()) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        okHttpClientBuilder.addInterceptor(AnalyticsInterceptor(::saveAnalytics))
        okHttpClientBuilder.dispatcher(createDispatcher())
        return okHttpClientBuilder
    }

    private fun saveAnalytics(isSuccessful: Boolean) {
        Preferences.persistent.updateTotalRequestCount(getApiName())
        if (!isSuccessful) {
            Preferences.persistent.updateErrorRequestCount(getApiName())
        }
    }

    abstract fun getApiName(): String

    protected open fun createDispatcher(): Dispatcher {
        val dispatcher = Dispatcher()
        dispatcher.maxRequests = DEFAULT_MAX_REQUESTS
        dispatcher.maxRequestsPerHost = DEFAULT_MAX_REQUESTS_PER_HOST
        return dispatcher
    }

    private class AnalyticsInterceptor(private val callback: (Boolean) -> Unit) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val response = chain.proceed(request)
            try {
                val isSuccessful = response.isSuccessful || response.isRedirect
                callback(isSuccessful)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return response
        }
    }
}
