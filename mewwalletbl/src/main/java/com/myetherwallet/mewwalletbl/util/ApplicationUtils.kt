package com.myetherwallet.mewwalletbl.util

import android.content.Context
import android.telephony.TelephonyManager
import com.myetherwallet.mewwalletbl.data.KeysStorageType
import com.myetherwallet.mewwalletbl.preference.Preferences
import java.util.*

/**
 * Created by BArtWell on 24.02.2020.
 */

object ApplicationUtils {

    fun getCountryIso(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = telephonyManager.networkCountryIso
        return if (networkCountryIso.isEmpty()) {
            Locale.getDefault().country
        } else {
            networkCountryIso
        }
    }

    fun getReferenceId(anonymousId: String): String {
        val market = if (Preferences.main.getStorageType() == KeysStorageType.SAMSUNG) "AndroidGalaxy" else "AndroidPlay"
        return "$market|$anonymousId"
    }
}
