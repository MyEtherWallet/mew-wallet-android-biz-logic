package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

enum class PurchaseState {
    @SerializedName("COMPLETED")
    COMPLETED,
    @SerializedName("COMPLETE")
    COMPLETE,
    @SerializedName("PROCESSING")
    PROCESSING,
    @SerializedName("FAILED")
    FAILED
}