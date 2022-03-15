package com.myetherwallet.mewwalletbl.core.api.ws.node.data

import com.myetherwallet.mewwalletbl.core.api.ws.data.BaseRequest

/**
 * Created by BArtWell on 15.02.2022.
 */

data class SubscribeRequest(
    val id: Int,
    val method: String,
    val params: List<String>
) : BaseRequest {

    val jsonrpc = "2.0"

    constructor(id: Int, method: Method, subscription: Subscription) : this(id, method.raw, listOf(subscription.raw))

    enum class Method(val raw: String) {
        SUBSCRIBE("eth_subscribe"),
        UNSUBSCRIBE("eth_unsubscribe")
    }
}
