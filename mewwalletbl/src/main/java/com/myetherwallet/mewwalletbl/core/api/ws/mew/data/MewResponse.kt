package com.myetherwallet.mewwalletbl.core.api.ws.mew.data

import com.myetherwallet.mewwalletbl.core.api.ws.data.BaseResponse

/**
 * Created by BArtWell on 02.09.2021.
 */

internal data class MewResponse<T>(
    override val id: Int,
    val response: MewResponseContent<T>
) : BaseResponse()
