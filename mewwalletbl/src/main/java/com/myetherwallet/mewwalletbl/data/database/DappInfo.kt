package com.myetherwallet.mewwalletbl.data.database

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DappInfo(
    val url: String,
    val title: String? = null,
    val desc: String? = null,
    val img: String? = null,
    val imgResource: Int? = null, // Temporary, remove when API for Featured will be available
    val category: String? = null,
    val timestamp: Date = Date(),
    val dappId: Long = 0
) : Parcelable