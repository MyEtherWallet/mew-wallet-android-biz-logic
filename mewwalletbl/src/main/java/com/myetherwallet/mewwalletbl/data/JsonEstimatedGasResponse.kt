package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

open class JsonEstimatedGasResponse(
    @SerializedName("result")
    var result: List<String>?
)