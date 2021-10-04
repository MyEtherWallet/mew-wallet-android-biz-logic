package com.myetherwallet.mewwalletbl.data.api.yearn

import com.google.gson.annotations.SerializedName

enum class YearnType {
    @SerializedName("DEPOSIT")
    DEPOSIT,
    @SerializedName("WITHDRAWAL")
    WITHDRAWAL
}