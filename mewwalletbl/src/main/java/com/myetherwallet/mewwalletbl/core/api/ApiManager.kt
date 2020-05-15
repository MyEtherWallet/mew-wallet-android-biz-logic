package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.core.api.analytics.AnalyticsApi
import com.myetherwallet.mewwalletbl.core.api.analytics.AnalyticsClient
import com.myetherwallet.mewwalletbl.core.api.dex.DexApi
import com.myetherwallet.mewwalletbl.core.api.dex.DexClient
import com.myetherwallet.mewwalletbl.core.api.mew.MewApi
import com.myetherwallet.mewwalletbl.core.api.mew.MewClient
import com.myetherwallet.mewwalletbl.core.api.node.NodeApi
import com.myetherwallet.mewwalletbl.core.api.node.NodeClient

/**
 * Created by BArtWell on 16.09.2019.
 */

object ApiManager {

    val nodeClient = NodeClient()
    val mewClient = MewClient()
    val dexClient = DexClient()
    val analyticsClient = AnalyticsClient()

    fun getNodeApi(): NodeApi = nodeClient.retrofit.create(NodeApi::class.java)

    fun getMewApi(): MewApi = mewClient.retrofit.create(MewApi::class.java)

    fun getDexApi(): DexApi = dexClient.retrofit.create(DexApi::class.java)

    fun getAnalyticsApi(): AnalyticsApi = analyticsClient.retrofit.create(AnalyticsApi::class.java)
}
