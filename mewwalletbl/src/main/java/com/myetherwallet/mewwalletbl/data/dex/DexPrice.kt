package com.myetherwallet.mewwalletbl.data.dex

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.myetherwallet.mewwalletbl.data.database.TokenDescription
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DexPrice(
    @SerializedName("price")
    val price: String,
    @SerializedName("dex")
    val dex: String,
    @SerializedName("pair")
    val pair: DexPair,
    @SerializedName("pair_description")
    val descriptionPair: DescriptionPair? = null,
    @SerializedName("marketImpact")
    val marketImpact: String? = null,
    @SerializedName("scale")
    val scale: Int? = null
) : Parcelable {
    @Parcelize
    class DexPair(
        @SerializedName("base")
        val base: String,
        @SerializedName("quote")
        val quote: String
    ) : Parcelable

    @Parcelize
    class DescriptionPair(
        @SerializedName("base")
        val base: TokenDescription,
        @SerializedName("quote")
        val quote: TokenDescription
    ) : Parcelable

    var availability = Status.AVAILABLE

    fun getPair(): Pair<String, String> {
        return Pair(this.pair.base, this.pair.quote)
    }

    enum class Status {
        AVAILABLE, PENDING, UNAVAILABLE
    }
}