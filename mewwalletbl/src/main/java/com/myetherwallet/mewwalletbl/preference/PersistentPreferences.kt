package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import com.myetherwallet.mewwalletbl.data.AppLanguage
import com.myetherwallet.mewwalletbl.util.ApplicationUtils

/**
 * Created by BArtWell on 05.08.2019.
 */

private const val PREFERENCES_NAME = "persistent"
private const val WHATS_NEW_DIALOG_VERSION = "whats_new_dialog_version"
private const val WAS_EXCHANGE_BADGE_SHOWN = "was_exchange_badge_shown"
private const val WAS_EXCHANGE_DISCLAIMER_SHOWN = "was_exchange_disclaimer_shown"
private const val SHOULD_FAIL_SWAPS = "should_fail_swaps"
private const val API_TOTAL_COUNT_PREFIX = "api_total_count_"
private const val API_ERROR_COUNT_PREFIX = "api_error_count_"
private const val APP_LANGUAGE = "app_language"
private const val GUIDE_BANNER_VERSION = "guide_banner_version"

class PersistentPreferences internal constructor(context: Context) {

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun shouldShowWhatsNewDialog(versionCode: Int): Boolean {
        val current = preferences.getInt(WHATS_NEW_DIALOG_VERSION, 0)
        preferences.edit().putInt(WHATS_NEW_DIALOG_VERSION, versionCode).apply()
        return current > 0 && current != versionCode
    }

    fun shouldShowGuideBanner(versionCode: Int): Boolean {
        val current = preferences.getInt(GUIDE_BANNER_VERSION, 0)
        preferences.edit().putInt(GUIDE_BANNER_VERSION, versionCode).apply()
        return current < 299
    }

    fun wasExchangeBadgeShown() = preferences.getBoolean(WAS_EXCHANGE_BADGE_SHOWN, false)

    fun setExchangeBadgeShown() = preferences.edit().putBoolean(WAS_EXCHANGE_BADGE_SHOWN, true).apply()

    fun wasExchangeDisclaimerShown() = preferences.getBoolean(WAS_EXCHANGE_DISCLAIMER_SHOWN, false)

    fun setExchangeDisclaimerShown() = preferences.edit().putBoolean(WAS_EXCHANGE_DISCLAIMER_SHOWN, true).apply()

    fun updateTotalRequestCount(apiName: String) = incrementIntValue(API_TOTAL_COUNT_PREFIX + "_" + apiName)

    fun getTotalRequestCount(apiName: String) = preferences.getInt(API_TOTAL_COUNT_PREFIX + "_" + apiName, 0)

    fun updateErrorRequestCount(apiName: String) = incrementIntValue(API_ERROR_COUNT_PREFIX + "_" + apiName)

    fun getErrorRequestCount(apiName: String) = preferences.getInt(API_ERROR_COUNT_PREFIX + "_" + apiName, 0)

    fun setFailSwaps(isEnable: Boolean) = preferences.edit().putBoolean(SHOULD_FAIL_SWAPS, isEnable).apply()

    fun shouldFailSwaps() = preferences.getBoolean(SHOULD_FAIL_SWAPS, false)

    private fun incrementIntValue(key: String) {
        val current = preferences.getInt(key, 0)
        preferences.edit().putInt(key, current + 1).apply()
    }

    fun setAppLanguage(appLanguage: AppLanguage) = preferences.edit().putString(APP_LANGUAGE, appLanguage.name).apply()

    fun getAppLanguage() = preferences.getString(APP_LANGUAGE, null)?.let { AppLanguage.valueOf(it) } ?: ApplicationUtils.getSystemLanguage()
}
