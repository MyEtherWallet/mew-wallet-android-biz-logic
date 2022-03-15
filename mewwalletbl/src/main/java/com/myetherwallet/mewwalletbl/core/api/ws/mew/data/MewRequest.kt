package com.myetherwallet.mewwalletbl.core.api.ws.mew.data

import com.myetherwallet.mewwalletbl.core.api.ws.data.BaseRequest

/**
 * Created by BArtWell on 02.09.2021.
 */

data class MewRequest<T>(
    val data: MewRequestContent<T>
) : BaseRequest {

    val action = "default"
}
