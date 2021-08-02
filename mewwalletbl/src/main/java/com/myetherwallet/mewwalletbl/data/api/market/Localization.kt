package com.myetherwallet.mewwalletbl.data.api.market

import android.content.Context
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Localization(

    @SerializedName("localization_key")
    val localizationKey: String,

    @SerializedName("title")
    val title: String? = null,

    @SerializedName("text")
    val text: String? = null,

    @SerializedName("url")
    val url: String? = null

) : Parcelable {

    fun getStringResource(context: Context): String {
        return try {
            if (Locale.getDefault().language == "ru") {
                context.resources.getString(context.resources.getIdentifier(localizationKey, "string", context.packageName))
            } else {
                getTitleOrText()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            getTitleOrText()
        }
    }

    private fun getTitleOrText() = title ?: text ?: ""
}