package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

/**
 * Created by BArtWell on 13.09.2019.
 */

open class JsonRpcResponse<T>(
    @SerializedName("result")
    var result: T?,
    @SerializedName("error")
    var error: JsonRpcResponseError?
)
