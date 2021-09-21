package com.myetherwallet.mewwalletbl.core.api.ws.data

/**
 * Created by BArtWell on 10.09.2021.
 */

data class ResponseContent<T>(
    val statusCode: Int,
    val body: T
)
