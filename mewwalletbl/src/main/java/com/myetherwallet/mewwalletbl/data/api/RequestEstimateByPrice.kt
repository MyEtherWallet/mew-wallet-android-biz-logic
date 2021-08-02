package com.myetherwallet.mewwalletbl.data.api

import com.google.gson.annotations.SerializedName

class RequestEstimateByPrice {

    @SerializedName("gasPrices")
    val prices = mutableListOf<String>()

    fun requestPrice(price: String) {
        prices.add(price)
    }

}