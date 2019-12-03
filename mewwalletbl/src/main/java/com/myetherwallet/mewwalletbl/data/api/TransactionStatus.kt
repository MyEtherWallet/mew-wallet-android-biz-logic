package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

enum class TransactionStatus {
    @SerializedName("FAIL")
    FAIL,
    @SerializedName("SUCCESS")
    SUCCESS,
    @SerializedName("PENDING")
    PENDING
}