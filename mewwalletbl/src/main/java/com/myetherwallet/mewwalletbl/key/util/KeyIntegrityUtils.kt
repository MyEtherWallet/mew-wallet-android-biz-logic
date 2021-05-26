package com.myetherwallet.mewwalletbl.key.util

import android.content.Context
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.data.MessageToSign
import com.myetherwallet.mewwalletbl.extension.getLongOrNull
import com.myetherwallet.mewwalletbl.extension.toDate
import com.myetherwallet.mewwalletbl.extension.toDateTimeZone
import com.myetherwallet.mewwalletbl.key.BaseEncryptHelper
import com.myetherwallet.mewwalletbl.key.BiometryEncryptHelper
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.PinEncryptHelper
import com.myetherwallet.mewwalletbl.key.storage.EncryptStorage
import com.myetherwallet.mewwalletbl.key.storage.PersistentEncryptStorage
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletbl.util.ApplicationUtils
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.core.extension.isNullOrEmpty
import com.myetherwallet.mewwalletkit.core.extension.secp256k1RecoverableSign
import com.myetherwallet.mewwalletkit.core.extension.secp256k1SerializeSignature

private const val TAG = "PrivateKeyUtil"
private const val TYPE_BIOMETRY = "biometry"
private const val TYPE_PIN = "pin"

object KeyIntegrityUtils {
    var lastCheckoutResult: Boolean? = null

    fun isAccessKeyAlive(
        context: Context,
        biometryEncryptHelper: BiometryEncryptHelper? = null,
        onCorrupted: (() -> Unit)? = null,
        onRestored: (() -> Unit)? = null,
        restore: Boolean = true
    ): Boolean {
        val encryptStorage = PersistentEncryptStorage()
        var accessKey = getMainAccessKey(context, encryptStorage)
        if (!restore) {
            return !accessKey.isNullOrEmpty()
        }
        if (accessKey.isNullOrEmpty()) {
            onCorrupted?.invoke()
            // Try to restore main key from backup
            restoreMainKeyFromBackups(context)
            accessKey = getMainAccessKey(context, encryptStorage)
        }
        if (accessKey.isNullOrEmpty()) {
            try {
                val biometryAccessKey = biometryEncryptHelper?.accessKey
                if (!biometryAccessKey.isNullOrEmpty()) {
                    accessKey = biometryAccessKey
                    saveMainAccessKey(context, biometryAccessKey!!)
                }
            } catch (e: Exception) {
                MewLog.e(TAG, "Cannot get biometry key", e)
            }
        }
        if (!accessKey.isNullOrEmpty()) {
            onRestored?.invoke()
            // Check backup keys and create or restore corrupted
            saveBackupKeys(
                context,
                encryptStorage,
                accessKey!!,
                false
            )
        }
        lastCheckoutResult = !accessKey.isNullOrEmpty()
        return lastCheckoutResult!!
    }

    fun isPrivateKeyAlive(encryptHelper: BaseEncryptHelper?): Boolean {
        var result = false
        Preferences.wallet.getWalletAddresses().firstOrNull()?.let { address ->
            Preferences.wallet.getPrivateKey(address)?.let { encryptedPrivateKey ->
                encryptHelper?.let {
                    result = canDecryptPrivateKey(encryptHelper, encryptedPrivateKey)
                }
            }
        }
        val type = when (encryptHelper) {
            is BiometryEncryptHelper -> TYPE_BIOMETRY
            is PinEncryptHelper -> TYPE_PIN
            else -> "unknown"
        }
        Preferences.main.savePrivateKeyTestDate(type, result)
        lastCheckoutResult = result
        return result
    }

    private fun getMainAccessKey(context: Context, encryptStorage: EncryptStorage): ByteArray? {
        return try {
            val encryptedAccessKey = encryptStorage.getAccessKey(KeyType.PIN)
            val keystoreHelper = KeystoreHelper(context, KeyType.PIN, encryptStorage)
            keystoreHelper.decrypt(encryptedAccessKey)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun isBackupKeyAlive(context: Context, index: Int, encryptStorage: EncryptStorage = PersistentEncryptStorage()) =
        !restoreAccessKeyFromBackup(context, encryptStorage, index).isNullOrEmpty()

    fun saveBackupKeys(context: Context, encryptStorage: EncryptStorage, accessKey: ByteArray, forceOverwrite: Boolean) {
        for (index in 0 until 10) {
            val keystoreHelper = BackupKeystoreHelper(context, index, encryptStorage)
            if (forceOverwrite || !isBackupKeyAlive(context, index, encryptStorage)) {
                val encryptedAccessKey = keystoreHelper.encrypt(accessKey)
                encryptStorage.addAccessKeyBackup(index, encryptedAccessKey)
            }
        }
    }

    private fun canDecryptPrivateKey(encryptHelper: BaseEncryptHelper, encryptedPrivateKey: ByteArray): Boolean {
        return try {
            val privateKey = encryptHelper.decrypt(encryptedPrivateKey).hexToByteArray()
            val hash = "4a5c5d454721bbbb25540c3317521e71c373ae36458f960d2ad46ef088110e95".hexToByteArray()
            val signature = hash.secp256k1RecoverableSign(privateKey)
            signature?.secp256k1SerializeSignature() != null
        } catch (e: Exception) {
            MewLog.e(TAG, "Cannot retrieve private key", e)
            false
        }
    }

    private fun restoreMainKeyFromBackups(context: Context) {
        val encryptStorage = PersistentEncryptStorage()
        for (index in 0 until 10) {
            val restoredAccessKey = restoreAccessKeyFromBackup(context, encryptStorage, index)
            if (!restoredAccessKey.isNullOrEmpty()) {
                saveMainAccessKey(context, restoredAccessKey!!)
                return
            }
        }
    }

    private fun saveMainAccessKey(context: Context, restoredAccessKey: ByteArray) {
        val encryptStorage = PersistentEncryptStorage()
        val keystoreHelper = KeystoreHelper(context, KeyType.PIN, encryptStorage)
        val encryptedAccessKey = keystoreHelper.encrypt(restoredAccessKey)
        encryptStorage.setAccessKey(encryptedAccessKey, KeyType.PIN)
    }

    private fun restoreAccessKeyFromBackup(context: Context, encryptStorage: EncryptStorage, index: Int): ByteArray? {
        try {
            val backupKeystoreHelper = BackupKeystoreHelper(context, index, encryptStorage)
            val accessKeyBackup = encryptStorage.getAccessKeyBackup(index)
            if (!accessKeyBackup.isNullOrEmpty()) {
                return backupKeystoreHelper.decrypt(accessKeyBackup!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getAccessKeyAliveStatus(context: Context) =
        "PIN access key is " +
                if (isAccessKeyAlive(context, null, null, null, false)) {
                    "alive"
                } else {
                    "dead"
                }

    fun getBackupKeysAliveStatus(context: Context): String {
        val buffer = StringBuilder()
        repeat(10) { index ->
            buffer.append("Backup #")
            buffer.append(index)
            buffer.append(" is ")
            if (isBackupKeyAlive(context, index)) {
                buffer.append("alive")
            } else {
                buffer.append("dead")
            }
            buffer.append("\n")
        }
        return buffer.toString()
    }

    fun createReport(context: Context): String {
        var result = getAccessKeyAliveStatus(context) + "\n"
        result += getBackupKeysAliveStatus(context)
        result += "Biometry: " + if (Preferences.main.isBiometryEnabled()) "enabled" else "disabled" + "\n"

        val jsonObject = Preferences.main.getPrivateKeyTestDates()

        result += "PIN last auth " + interpretateTestDate(jsonObject.getLongOrNull(TYPE_PIN)) + "\n"
        result += "Biometry last auth " + interpretateTestDate(jsonObject.getLongOrNull(TYPE_BIOMETRY)) + "\n"

        ApplicationUtils.getAppInstallTime(context)?.let {
            result += "Installed at " + it.toDate().toDateTimeZone() + "\n"
        }
        return result
    }

    private fun interpretateTestDate(date: Long?) = if (date == null) {
        "no info"
    } else if (date == 0L) {
        "failed"
    } else {
        "success at " + date.toDate().toDateTimeZone()
    }
}
