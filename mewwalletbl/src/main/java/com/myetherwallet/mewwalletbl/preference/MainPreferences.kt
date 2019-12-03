package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import android.util.Base64
import androidx.preference.PreferenceManager
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.util.Utils
import java.util.*

/**
 * Created by BArtWell on 05.08.2019.
 */

private const val IS_BETA_WARNING_HIDDEN = "is_beta_warning_hidden"
private const val AUTH_FIRST_ATTEMPT_TIME = "auth_first_attempt_time"
private const val AUTH_ATTEMPTS_COUNT = "auth_attempts_count"
private const val AUTH_TIMER_TIME = "auth_timer_time"
private const val BIOMETRY_ENABLE = "biometry_enable"
private const val ACCESS_KEY = "access_key_"
private const val MASTER_KEY = "master_key_"
private const val SIGNATURE = "signature_"
private const val KEYSTORE_IV = "keystore_iv_"
private const val BIOMETRY_SALT = "biometry_salt"
private const val GUIDE_ENABLE = "guide_enable"
private const val FCM_TOKEN = "fcm_token"

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

    fun setGuideEnabled(isEnable: Boolean) = preferences.edit().putBoolean(GUIDE_ENABLE, isEnable).apply()

    fun isGuideEnabled() = preferences.getBoolean(GUIDE_ENABLE, true)

    fun setBetaWarningHidden(isEnable: Boolean) = preferences.edit().putBoolean(IS_BETA_WARNING_HIDDEN, isEnable).apply()

    fun isBetaWarningHidden() = preferences.getBoolean(IS_BETA_WARNING_HIDDEN, false)

    fun setFcmToken(token: String) = preferences.edit().putString(FCM_TOKEN, token).apply()

    fun getFcmToken() = preferences.getString(FCM_TOKEN, null)

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

    internal fun getBiometrySalt(): ByteArray {
        val salt = readBytes(BIOMETRY_SALT) ?: Utils.getRandomBytes(16)
        saveBytes(BIOMETRY_SALT, salt)
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
