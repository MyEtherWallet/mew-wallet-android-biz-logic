package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 12.02.2020.
 */

data class PurchaseProviderResponse(
    @SerializedName("web")
    val web: PurchaseProvider
)