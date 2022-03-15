package com.myetherwallet.mewwalletbl.core.api.ws.mew.data

/**
 * Created by BArtWell on 10.09.2021.
 */

data class MewResponseContent<T>(
    val statusCode: Int,
    val body: T
)
