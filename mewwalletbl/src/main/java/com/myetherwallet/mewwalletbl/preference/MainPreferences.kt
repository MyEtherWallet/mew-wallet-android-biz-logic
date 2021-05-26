package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import android.util.Base64
import androidx.preference.PreferenceManager
import com.myetherwallet.mewwalletbl.MewEnvironment
import com.myetherwallet.mewwalletbl.data.KeysStorageType
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.util.Utils
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

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
private const val ACCESS_KEY_BACKUP = "access_key_backup_"
private const val KEYSTORE_IV_BACKUP = "keystore_iv_backup_"
private const val SIGNATURE = "signature_"
private const val KEYSTORE_IV = "keystore_iv_"
private const val BIOMETRY_SALT = "biometry_salt"
private const val GUIDE_ENABLE = "guide_enable"
private const val IS_SECURE_MODE_ENABLED = "is_secure_mode_enabled"
private const val FCM_TOKEN = "fcm_token"
private const val STORAGE_TYPE = "storage_type"
private const val SAMSUNG_BLOCKCHAIN_HASH = "samsung_blockchain_hash"
private const val DEBUG_ENVIRONMENT_TYPE = "debug_environment_type"
private const val FORCE_BACKUP_DIALOG_DATE = "force_backup_dialog_date"
private const val WAS_APP_CRASHED = "was_app_crashed"
private const val APP_CRASH_EXCEPTION = "app_crash_exception"
private const val RATE_STARTS_COUNT = "rate_starts_count"
private const val RATE_STARTS_THRESHOLD = "rate_starts_threshold"
private const val IS_TOKENS_ICONS_CACHED = "is_tokens_icons_cached"
private const val PRIVATE_KEY_TEST_DATE = "private_key_test_date"
private const val DEFAULT_RATE_STARTS_THRESHOLD = 20
private const val IS_DEBUG_RATER_SHOWN = "is_debug_rater_shown"
private const val NO_CRASH_SESSION_COUNT = "no_crash_session_count"
private const val DEBUG_WALLET_BALANCE = "debug_wallet_balance"
private const val DEBUG_DISABLE_STAKED_TRANSACTION = "debug_disable_staked_transaction"
private const val DAPP_RADAR_UPDATE_TIME = "dapp_radar_update_time"
private const val FIRST_ACCOUNT_NACL_PUBLIC_KEY = "first_account_nacl_public_key"
private const val INTERCOM_HASH = "intercom_hash"
private const val WAS_MARKET_BADGE_SHOWN = "was_market_badge_shown"
private const val WAS_EXCHANGE_DISCLAIMER_SHOWN = "was_exchange_disclaimer_shown"
private const val WAS_DAPP_DISCLAIMER_SHOWN = "was_dapp_disclaimer_shown"
private const val WAS_DAPP_CATALOG_DISCLAIMER_SHOWN = "was_dapp_catalog_disclaimer_shown"
private const val GUIDE_BANNER_VERSION = "guide_banner_version"
private const val WAS_STAKING_EXITED_SHOWN = "was_staking_exited_shown"

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

    fun shouldShowForceBackupDialog(): Boolean {
        val saved = preferences.getLong(FORCE_BACKUP_DIALOG_DATE, 0)
        if (System.currentTimeMillis() - saved > TimeUnit.DAYS.toMillis(1)) {
            return true
        }
        return false
    }

    fun setBackupDialogShown() {
        preferences.edit().putLong(FORCE_BACKUP_DIALOG_DATE, System.currentTimeMillis()).apply()
    }

    fun getDappRadarUpdateTime() = preferences.getLong(DAPP_RADAR_UPDATE_TIME, 0)

    fun setDappRadarUpdateTime(date: Date) = preferences.edit().putLong(DAPP_RADAR_UPDATE_TIME, date.time).apply()

    internal fun setAccessKeyBackup(index: Int, accessKey: ByteArray) = saveBytes(ACCESS_KEY_BACKUP + index, accessKey)

    internal fun getAccessKeyBackup(index: Int) = readBytes(ACCESS_KEY_BACKUP + index)

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

    internal fun getKeystoreIvBackup(index: Int): ByteArray = readBytes(KEYSTORE_IV_BACKUP + index)!!

    internal fun setKeystoreIvBackup(index: Int, iv: ByteArray) = saveBytes(KEYSTORE_IV_BACKUP + index, iv)

    internal fun saveSalt(salt: ByteArray) = saveBytes(BIOMETRY_SALT, salt)

    internal fun getBiometrySalt(): ByteArray {
        val salt = readBytes(BIOMETRY_SALT) ?: Utils.getRandomBytes(16)
        saveSalt(salt)
        return salt
    }

    internal fun removeBiometrySalt() = remove(BIOMETRY_SALT)

    fun getFirstAccountNaclPublicKey(): ByteArray? = readBytes(FIRST_ACCOUNT_NACL_PUBLIC_KEY)

    fun setFirstAccountNaclPublicKey(key: ByteArray) = saveBytes(FIRST_ACCOUNT_NACL_PUBLIC_KEY, key)

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

    fun savePrivateKeyTestDate(type: String, isAlive: Boolean) {
        val jsonObject = getPrivateKeyTestDates()
        if (isAlive) {
            jsonObject.put(type, Date().time)
        } else {
            jsonObject.put(type, 0L)
        }
        preferences.edit().putString(PRIVATE_KEY_TEST_DATE, jsonObject.toString()).apply()
    }

    fun getPrivateKeyTestDates() = preferences.getString(PRIVATE_KEY_TEST_DATE, null)?.let { JSONObject(it) } ?: JSONObject()

    fun setDebugRaterShown(isShown: Boolean) = preferences.edit().putBoolean(IS_DEBUG_RATER_SHOWN, isShown).apply()

    fun isDebugRaterShown() = preferences.getBoolean(IS_DEBUG_RATER_SHOWN, false)

    fun setNoCrashSessionCount(count: Int) = preferences.edit().putInt(NO_CRASH_SESSION_COUNT, count).apply()

    fun getNoCrashSessionCount() = preferences.getInt(NO_CRASH_SESSION_COUNT, 0)

    fun getDebugWalletBalance() = preferences.getFloat(DEBUG_WALLET_BALANCE, 0f)

    fun setDebugWalletBalance(debugBalance: Float) = preferences.edit().putFloat(DEBUG_WALLET_BALANCE, debugBalance).apply()

    fun isStakedTransactionDisabled() = preferences.getBoolean(DEBUG_DISABLE_STAKED_TRANSACTION, false)

    fun setStakedTransactionDisabled(isDisabled: Boolean) = preferences.edit().putBoolean(DEBUG_DISABLE_STAKED_TRANSACTION, isDisabled).apply()

    fun setIntercomHash(hash: String) = preferences.edit().putString(INTERCOM_HASH, hash).apply()

    fun getIntercomHash() = preferences.getString(INTERCOM_HASH, null)

    fun wasMarketBadgeShown() = preferences.getBoolean(WAS_MARKET_BADGE_SHOWN, false)

    fun setMarketBadgeShown() = preferences.edit().putBoolean(WAS_MARKET_BADGE_SHOWN, true).apply()

    fun wasExchangeDisclaimerShown() = preferences.getBoolean(WAS_EXCHANGE_DISCLAIMER_SHOWN, false)

    fun setExchangeDisclaimerShown() = preferences.edit().putBoolean(WAS_EXCHANGE_DISCLAIMER_SHOWN, true).apply()

    fun setDappDisclaimerShown(host: String) {
        val hosts = preferences.getString(WAS_DAPP_DISCLAIMER_SHOWN, null)?.split(",")?.toMutableList() ?: mutableListOf()
        if (!hosts.contains(host)) {
            hosts.add(host)
            preferences.edit().putString(WAS_DAPP_DISCLAIMER_SHOWN, hosts.joinToString(",")).apply()
        }
    }

    fun wasDappDisclaimerShown(host: String) = preferences.getString(WAS_DAPP_DISCLAIMER_SHOWN, null)?.split(",")?.contains(host) ?: false

    fun wasDappCatalogDisclaimerShown() = preferences.getBoolean(WAS_DAPP_CATALOG_DISCLAIMER_SHOWN, false)

    fun setDappCatalogDisclaimerShown() = preferences.edit().putBoolean(WAS_DAPP_CATALOG_DISCLAIMER_SHOWN, true).apply()

    fun shouldShowGuideBanner(versionCode: Int): Boolean {
        val current = preferences.getInt(GUIDE_BANNER_VERSION, 0)
        preferences.edit().putInt(GUIDE_BANNER_VERSION, versionCode).apply()
        return current < 299
    }

    fun wasStakingExitedShown() = preferences.getBoolean(WAS_STAKING_EXITED_SHOWN, false)

    fun setStakingExitedShown() = preferences.edit().putBoolean(WAS_STAKING_EXITED_SHOWN, true).apply()
}
