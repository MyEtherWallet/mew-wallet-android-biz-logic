package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import android.util.Base64
import androidx.preference.PreferenceManager
import com.myetherwallet.mewwalletbl.MewEnvironment
import com.myetherwallet.mewwalletbl.data.KeysStorageType
import com.myetherwallet.mewwalletbl.data.PurchaseProvider
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.util.Utils
import java.util.*

/**
 * Created by BArtWell on 05.08.2019.
 */

private const val AUTH_FIRST_ATTEMPT_TIME = "auth_first_attempt_time"
private const val AUTH_ATTEMPTS_COUNT = "auth_attempts_count"
private const val AUTH_TIMER_TIME = "auth_timer_time"
private const val BIOMETRY_ENABLE = "biometry_enable"
private const val WAS_IMPORT_SUGGESTED = "was_import_suggested"
private const val ACCESS_KEY = "access_key_"
private const val MASTER_KEY = "master_key_"
private const val SIGNATURE = "signature_"
private const val KEYSTORE_IV = "keystore_iv_"
private const val BIOMETRY_SALT = "biometry_salt"
private const val GUIDE_ENABLE = "guide_enable"
private const val IS_SECURE_MODE_ENABLED = "is_secure_mode_enabled"
private const val FCM_TOKEN = "fcm_token"
private const val STORAGE_TYPE = "storage_type"
private const val SAMSUNG_BLOCKCHAIN_HASH = "samsung_blockchain_hash"
private const val DEBUG_ENVIRONMENT_TYPE = "debug_environment_type"
private const val DEBUG_PURCHASE_PROVIDER = "debug_purchase_provider"
private const val PURCHASE_PROVIDER = "purchase_provider"
private const val WAS_APP_CRASHED = "was_app_crashed"
private const val APP_CRASH_EXCEPTION = "app_crash_exception"
private const val RATE_STARTS_COUNT = "rate_starts_count"
private const val RATE_STARTS_THRESHOLD = "rate_starts_threshold"
private const val IS_TOKENS_ICONS_CACHED = "is_tokens_icons_cached"
private const val DEFAULT_RATE_STARTS_THRESHOLD = 10

class MainPreferences internal constructor(context: Context) {

    private val preferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun getAuthFirstAttemptTime() = preferences.getLong(AUTH_FIRST_ATTEMPT_TIME, 0L)

    fun setAuthFirstAttemptTime(uptime: Long) = preferences.edit().putLong(AUTH_FIRST_ATTEMPT_TIME, uptime).apply()

    fun getAuthAttemptsCount() = preferences.getInt(AUTH_ATTEMPTS_COUNT, 0)

    fun incrementAuthAttemptsCount() = preferences.edit().putInt(AUTH_ATTEMPTS_COUNT, getAuthAttemptsCount() + 1).apply()

    fun resetAuthAttemptsCount() = preferences.edit().remove(AUTH_ATTEMPTS_COUNT).apply()

    fun getAuthTimerTime() = preferences.getLong(AUTH_TIMER_TIME, 0)

    fun setAuthTimerTime(uptime: Long) = preferences.edit().putLong(AUTH_TIMER_TIME, uptime).apply()

    fun resetAuthTimerTime() = preferences.edit().remove(AUTH_TIMER_TIME).apply()

    fun setBiometryEnabled(isEnable: Boolean) = preferences.edit().putBoolean(BIOMETRY_ENABLE, isEnable).apply()

    fun isBiometryEnabled() = preferences.getBoolean(BIOMETRY_ENABLE, false)

    fun setImportSuggested(suggested: Boolean) = preferences.edit().putBoolean(WAS_IMPORT_SUGGESTED, suggested).apply()

    fun wasImportSuggested() = preferences.getBoolean(WAS_IMPORT_SUGGESTED, false)

    fun setGuideEnabled(isEnable: Boolean) = preferences.edit().putBoolean(GUIDE_ENABLE, isEnable).apply()

    fun isGuideEnabled() = preferences.getBoolean(GUIDE_ENABLE, true)

    fun setSecureModeEnabled(isEnable: Boolean) = preferences.edit().putBoolean(IS_SECURE_MODE_ENABLED, isEnable).apply()

    fun isSecureModeEnabled() = preferences.getBoolean(IS_SECURE_MODE_ENABLED, true)

    fun setFcmToken(token: String) = preferences.edit().putString(FCM_TOKEN, token).apply()

    fun getFcmToken() = preferences.getString(FCM_TOKEN, null)

    fun setStorageType(type: KeysStorageType?) = preferences.edit().putString(STORAGE_TYPE, type?.name).apply()

    fun getStorageType() = preferences.getString(STORAGE_TYPE, null)?.let { KeysStorageType.valueOf(it) }

    fun getSamsungBlockchainHash() = preferences.getString(SAMSUNG_BLOCKCHAIN_HASH, null)

    fun setSamsungBlockchainHash(hash: String?) = preferences.edit().putString(SAMSUNG_BLOCKCHAIN_HASH, hash).apply()

    fun setDebugEnvironmentType(type: MewEnvironment.Type) = preferences.edit().putString(DEBUG_ENVIRONMENT_TYPE, type.name).apply()

    fun getDebugEnvironmentType() = MewEnvironment.Type.valueOf(preferences.getString(DEBUG_ENVIRONMENT_TYPE, MewEnvironment.Type.PRODUCTION.name)!!)

    fun setDebugPurchaseProvider(provider: PurchaseProvider?) = preferences.edit().putString(DEBUG_PURCHASE_PROVIDER, provider?.name).apply()

    fun getDebugPurchaseProvider() = preferences.getString(DEBUG_PURCHASE_PROVIDER, null)?.let { PurchaseProvider.valueOf(it) }

    fun setPurchaseProvider(provider: PurchaseProvider) = preferences.edit().putString(PURCHASE_PROVIDER, provider.name).apply()

    fun getPurchaseProvider(): PurchaseProvider {
        var provider = preferences.getString(DEBUG_PURCHASE_PROVIDER, null)
        if (provider == null) {
            provider = preferences.getString(PURCHASE_PROVIDER, PurchaseProvider.SIMPLEX.name)!!
        }
        return PurchaseProvider.valueOf(provider)
    }

    fun setAppCrashed(wasCrashed: Boolean) = preferences.edit().putBoolean(WAS_APP_CRASHED, wasCrashed).apply()

    fun wasAppCrashed() = preferences.getBoolean(WAS_APP_CRASHED, false)

    fun setAppCrashException(text: String) = preferences.edit().putString(APP_CRASH_EXCEPTION, text).apply()

    fun getAppCrashException() = preferences.getString(APP_CRASH_EXCEPTION, "")

    fun getRateStartsCount() = preferences.getInt(RATE_STARTS_COUNT, 0)

    fun setRateStartsCount(count: Int) = preferences.edit().putInt(RATE_STARTS_COUNT, count).apply()

    fun setRateStartsThreshold(count: Int) = preferences.edit().putInt(RATE_STARTS_THRESHOLD, count).apply()

    fun getRateStartsThreshold() = preferences.getInt(RATE_STARTS_THRESHOLD, DEFAULT_RATE_STARTS_THRESHOLD)

    fun isTokensIconsCached() = preferences.getBoolean(IS_TOKENS_ICONS_CACHED, false)

    fun setTokensIconsCached() = preferences.edit().putBoolean(IS_TOKENS_ICONS_CACHED, true).apply()

    internal fun setAccessKey(key: ByteArray, keyType: KeyType) = saveBytes(ACCESS_KEY + keyType.name.toLowerCase(Locale.US), key)

    internal fun getAccessKey(keyType: KeyType): ByteArray = readBytes(ACCESS_KEY + keyType.name.toLowerCase(Locale.US))!!

    internal fun isAccessKeyExists(keyType: KeyType) = preferences.contains(ACCESS_KEY + keyType.name.toLowerCase(Locale.US))

    internal fun removeAccessKey(keyType: KeyType) = remove(ACCESS_KEY + keyType.name.toLowerCase(Locale.US))

    internal fun setMasterKey(key: ByteArray, keyType: KeyType) = saveBytes(MASTER_KEY + keyType.name.toLowerCase(Locale.US), key)

    internal fun getMasterKey(keyType: KeyType): ByteArray = readBytes(MASTER_KEY + keyType.name.toLowerCase(Locale.US))!!

    internal fun removeMasterKey(keyType: KeyType) = remove(MASTER_KEY + keyType.name.toLowerCase(Locale.US))

    internal fun setSignature(key: ByteArray, keyType: KeyType) = saveBytes(SIGNATURE + keyType.name.toLowerCase(Locale.US), key)

    internal fun getSignature(keyType: KeyType): ByteArray? = readBytes(SIGNATURE + keyType.name.toLowerCase(Locale.US))

    internal fun setKeystoreIv(keyType: KeyType, iv: ByteArray) = saveBytes(KEYSTORE_IV + keyType.name.toLowerCase(Locale.US), iv)

    internal fun getKeystoreIv(keyType: KeyType): ByteArray = readBytes(KEYSTORE_IV + keyType.name.toLowerCase(Locale.US))!!

    internal fun saveSalt(salt: ByteArray) = saveBytes(BIOMETRY_SALT, salt)

    internal fun getBiometrySalt(): ByteArray {
        val salt = readBytes(BIOMETRY_SALT) ?: Utils.getRandomBytes(16)
        saveSalt(salt)
        return salt
    }

    internal fun removeBiometrySalt() = remove(BIOMETRY_SALT)

    private fun saveBytes(key: String, data: ByteArray) {
        preferences
            .edit()
            .putString(key, Base64.encodeToString(data, Base64.DEFAULT))
            .apply()
    }

    private fun readBytes(key: String) = preferences.getString(key, null)?.let {
        Base64.decode(it, Base64.DEFAULT)
    }

    private fun remove(key: String) {
        preferences
            .edit()
            .remove(key)
            .apply()
    }

    fun removeAllData() {
        preferences.edit().clear().apply()
    }
}
