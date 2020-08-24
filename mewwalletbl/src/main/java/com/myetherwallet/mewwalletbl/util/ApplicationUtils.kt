package com.myetherwallet.mewwalletbl.util

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.telephony.TelephonyManager
import com.myetherwallet.mewwalletbl.data.AppLanguage
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

    fun getSystemLanguage() = when (getSystemLocale().language) {
        Locale("ru").language -> AppLanguage.RUSSIAN
        else -> AppLanguage.ENGLISH
    }

    private fun getSystemLocale(): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            return Resources.getSystem().configuration.locale
        }
    }

    fun setAppLocale(context: Context, language: AppLanguage) {
        val locale = Locale(language.code)
        val config = Configuration(context.resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}
