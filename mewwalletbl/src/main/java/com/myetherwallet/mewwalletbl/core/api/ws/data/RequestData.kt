package com.myetherwallet.mewwalletbl.core.api.ws.data

/**
 * Created by BArtWell on 13.09.2021.
 */

data class RequestData<T>(
    val id: Int,
    val method: String,
    val path: String,
    val query: T
)
