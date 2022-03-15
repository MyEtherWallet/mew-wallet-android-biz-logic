package com.myetherwallet.mewwalletbl.core.api.ws.node.data

/**
 * Created by BArtWell on 15.02.2022.
 */

data class NodeEventParams<T>(
    val subscription: String,
    val result: T
)
