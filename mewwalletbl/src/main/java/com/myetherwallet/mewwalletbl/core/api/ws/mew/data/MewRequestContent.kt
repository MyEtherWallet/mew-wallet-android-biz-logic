package com.myetherwallet.mewwalletbl.core.api.ws.mew.data

/**
 * Created by BArtWell on 13.09.2021.
 */

data class MewRequestContent<T>(
    val id: Int,
    val method: String,
    val path: String,
    val query: T
)
