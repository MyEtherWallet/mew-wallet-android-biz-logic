package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 11.02.2022.
 */

data class DomainCheckoutResult(
    @SerializedName("safe")
    val safe: Boolean
)
