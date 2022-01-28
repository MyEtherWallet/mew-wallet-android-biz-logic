package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.preference.Preferences
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

/**
 * Created by BArtWell on 10.04.2020.
 */

private const val DEFAULT_MAX_REQUESTS = 100
private const val DEFAULT_MAX_REQUESTS_PER_HOST = 25
private const val TIMEOUT = 30L

private const val TAG = "BaseClient"

abstract class BaseClient : Client {

    val client by lazy { createClient() }

    private fun createClient() = setupClient().build()

    open fun setupClient(): OkHttpClient.Builder {
        val okHttpClientBuilder = OkHttpClient.Builder()
        if (MewLog.shouldDisplayLogs()) {
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            okHttpClientBuilder.addInterceptor(loggingInterceptor)
        }
        if (!BuildConfig.IS_MASTER_VERSION) {
            try {
                val clazz = Class.forName("ru.bartwell.loggy.LoggyInterceptor")
                val loggy = clazz.getConstructor().newInstance()
                okHttpClientBuilder.addInterceptor(loggy as Interceptor)
            } catch (e: Exception) {
                MewLog.w(TAG, "Unable to init loggy", e)
            }
        }
        if (BuildConfig.DEBUG) {
            trustAnySslCertificate(okHttpClientBuilder)
        }
//        Cause 'IOException: Canceled' with some servers
//        okHttpClientBuilder.callTimeout(TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
////        SocketTimeoutException: https://stackoverflow.com/a/62031983/1393280
//        okHttpClientBuilder.protocols(listOf(Protocol.HTTP_1_1))
        okHttpClientBuilder.addInterceptor(AnalyticsInterceptor(::saveAnalytics))
        okHttpClientBuilder.dispatcher(createDispatcher())
        return okHttpClientBuilder
    }

    private fun trustAnySslCertificate(okHttpClientBuilder: OkHttpClient.Builder) {
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return emptyArray()
                }
            }
        )
        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        okHttpClientBuilder.hostnameVerifier { _, _ -> true }
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
