package com.myetherwallet.mewwalletbl.data.api.market

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarketCollectionItem(
    @SerializedName("entry_title")
    val entryTitle: String,

    @SerializedName("title")
    val title: Localization,

    @SerializedName("description")
    val description: Localization,

    @SerializedName("short_title")
    val shortTitle: Localization,

    @SerializedName("short_description")
    val shortDescription: Localization,

    @SerializedName("action")
    val action: Localization,

    @SerializedName("banner")
    val banner: Banner,

    @SerializedName("theme")
    val theme: Theme,

    @SerializedName("tokens")
    val tokens: List<MarketItem>,

    @SerializedName("filters")
    val filters: List<CollectionFilter>

) : Parcelable {

    @Parcelize
    data class Banner(
        @SerializedName("small")
        val small: String,
        @SerializedName("big")
        val big: String
    ) : Parcelable

    @Parcelize
    enum class Theme : Parcelable {
        @SerializedName("LIGHT")
        LIGHT,

        @SerializedName("DARK")
        DARK
    }

    fun isDarkTheme() = theme == Theme.DARK
}