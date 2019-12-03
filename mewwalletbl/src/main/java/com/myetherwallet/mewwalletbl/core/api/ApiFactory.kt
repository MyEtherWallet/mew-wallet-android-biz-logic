package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.core.api.node.NodeApi
import com.myetherwallet.mewwalletbl.core.api.mew.MewApi
import com.myetherwallet.mewwalletbl.core.api.mew.MewClient
import com.myetherwallet.mewwalletbl.core.api.node.NodeClient

/**
 * Created by BArtWell on 16.09.2019.
 */

internal object ApiFactory {

    fun getNodeApi(): NodeApi = NodeClient().retrofit.create(NodeApi::class.java)

    fun getMewApi(): MewApi = MewClient().retrofit.create(MewApi::class.java)
}
