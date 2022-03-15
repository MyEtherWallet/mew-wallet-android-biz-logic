package com.myetherwallet.mewwalletbl.core.api.ws.mew.data

import com.myetherwallet.mewwalletbl.core.api.ws.data.ResponseErrorContent

/**
 * Created by BArtWell on 10.09.2021.
 */

data class MewResponseErrorContent(
    val error: String?
) : ResponseErrorContent()
