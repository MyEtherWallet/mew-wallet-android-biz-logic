package com.myetherwallet.mewwalletbl.core.api.node

import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.api.BaseClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by BArtWell on 13.09.2019.
 */

class NodeClient : BaseClient() {

    override val retrofit = createRetrofit()

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.NODE_API_END_POINT)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun getApiName() = NAME

    companion object {
        const val NAME = "Node"
    }
}
