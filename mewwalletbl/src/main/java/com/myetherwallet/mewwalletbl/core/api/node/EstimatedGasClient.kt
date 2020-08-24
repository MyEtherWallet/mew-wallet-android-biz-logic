package com.myetherwallet.mewwalletbl.core.api.node

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.BaseClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EstimatedGasClient : BaseClient() {

    override val retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.ESTIMATED_GAS_API_END_POINT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun getApiName() = NAME

    companion object {
        const val NAME = "EstimatedGas"
    }
}