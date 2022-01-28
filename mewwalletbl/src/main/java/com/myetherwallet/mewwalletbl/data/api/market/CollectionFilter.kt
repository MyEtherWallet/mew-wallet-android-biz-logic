package com.myetherwallet.mewwalletbl.data.api.market

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class CollectionFilter(
    @SerializedName("title")
    val title: Localization,
    @SerializedName("tags")
    val tags: List<Localization>
) : Parcelable