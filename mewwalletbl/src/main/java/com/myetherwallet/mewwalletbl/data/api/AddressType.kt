package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

enum class AddressType {
    @SerializedName("ADDRESS")
    ADDRESS,
    @SerializedName("TOKEN")
    TOKEN,
    @SerializedName("CONTRACT")
    CONTRACT
}
