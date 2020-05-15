package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName

data class PurchaseHistory(
    @SerializedName("results")
    val results: List<PurchaseHistoryItem>,
    @SerializedName("pagination_token")
    val paginationToken: String
)
