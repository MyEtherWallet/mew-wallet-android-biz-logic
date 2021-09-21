package com.myetherwallet.mewwalletbl.core.api.ws.data

import java.util.*

/**
 * Created by BArtWell on 02.09.2021.
 */

enum class Method {
    GET, POST, PUT, DELETE;

    internal fun getMethod() = this.name.lowercase(Locale.US)
}
