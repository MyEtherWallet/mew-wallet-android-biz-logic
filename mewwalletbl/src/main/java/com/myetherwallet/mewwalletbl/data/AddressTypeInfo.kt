package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.api.AddressType

class AddressTypeInfo (
    @SerializedName("address")
    val address: String,
    @SerializedName("type")
    val type: AddressType
)
