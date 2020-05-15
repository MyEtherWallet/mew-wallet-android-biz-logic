package com.myetherwallet.mewwalletbl.core.api.analytics

import com.google.gson.GsonBuilder
import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.BaseClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by BArtWell on 21.09.2019.
 */

class AnalyticsClient : BaseClient() {

    override val retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        val gson = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .create()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.ANALYTICS_END_POINT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    override fun getApiName() = NAME

    companion object {
        const val NAME = "Analytics"
    }
}
