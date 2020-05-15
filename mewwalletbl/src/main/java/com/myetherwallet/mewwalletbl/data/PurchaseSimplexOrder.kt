package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 12.02.2020.
 */

data class PurchaseSimplexOrder(
    @SerializedName("url")
    val url: String,
    @SerializedName("form")
    val form: Map<String, String>,
    @SerializedName("form_encoded")
    val formEncoded: String
)
