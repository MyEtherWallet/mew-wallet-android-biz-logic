package com.myetherwallet.mewwalletbl.core.api.ws.node.data

/**
 * Created by BArtWell on 15.02.2022.
 */

data class NodeEvent<T>(
    val method: String,
    val params: NodeEventParams<T>
) {

    companion object {

        const val SUBSCRIPTION_METHOD = "eth_subscription"
    }
}
