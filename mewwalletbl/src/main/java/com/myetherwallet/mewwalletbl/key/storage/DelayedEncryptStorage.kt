package com.myetherwallet.mewwalletbl.key.storage

import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.util.Utils
import com.myetherwallet.mewwalletbl.preference.Preferences

class DelayedEncryptStorage : EncryptStorage {

    private var isSaved = false

    private var keystoreIvBiometry: ByteArray? = null
    private var keystoreIvPin: ByteArray? = null

    private var encryptedAccessKeyBiometry: ByteArray? = null
    private var encryptedAccessKeyPin: ByteArray? = null

    private var encryptedMasterKeyBiometry: ByteArray? = null
    private var encryptedMasterKeyPin: ByteArray? = null

    private var signatureBiometry: ByteArray? = null
    private var signaturePin: ByteArray? = null

    private var biometrySalt: ByteArray? = null

    private var isBiometryEnabled: Boolean? = null

    private val accessKeyBackups = mutableMapOf<Int, ByteArray>()
    private val keystoreIvBackups = mutableMapOf<Int, ByteArray>()

    override fun setKeystoreIv(keyType: KeyType, iv: ByteArray) {
        when (keyType) {
            KeyType.BIOMETRY -> keystoreIvBiometry = iv
            else -> keystoreIvPin = iv
        }
    }

    override fun setAccessKey(key: ByteArray, keyType: KeyType) {
        when (keyType) {
            KeyType.BIOMETRY -> encryptedAccessKeyBiometry = key
            else -> encryptedAccessKeyPin = key
        }
    }

    override fun setMasterKey(key: ByteArray, keyType: KeyType) {
        when (keyType) {
            KeyType.BIOMETRY -> encryptedMasterKeyBiometry = key
            else -> encryptedMasterKeyPin = key
        }
    }

    override fun setSignature(key: ByteArray, keyType: KeyType) {
        when (keyType) {
            KeyType.BIOMETRY -> signatureBiometry = key
            else -> signaturePin = key
        }
    }

    override fun getBiometrySalt(): ByteArray {
        return biometrySalt ?: run {
            val salt = Utils.getRandomBytes(16)
            biometrySalt = salt
            salt
        }
    }

    override fun getKeystoreIv(keyType: KeyType) = when (keyType) {
        KeyType.BIOMETRY -> keystoreIvBiometry
        else -> keystoreIvPin
    }!!

    override fun getAccessKey(keyType: KeyType) = when (keyType) {
        KeyType.BIOMETRY -> encryptedAccessKeyBiometry
        else -> encryptedAccessKeyPin
    }!!

    override fun getMasterKey(keyType: KeyType) = when (keyType) {
        KeyType.BIOMETRY -> encryptedMasterKeyBiometry
        else -> encryptedMasterKeyPin
    }!!

    override fun isAccessKeyExists(keyType: KeyType) = when (keyType) {
        KeyType.BIOMETRY -> encryptedAccessKeyBiometry
        else -> encryptedAccessKeyPin
    } != null

    override fun getSignature(keyType: KeyType) = when (keyType) {
        KeyType.BIOMETRY -> signatureBiometry
        else -> signaturePin
    }

    override fun removeBiometrySalt() {
        biometrySalt = null
    }

    override fun removeAccessKey(keyType: KeyType) {
        when (keyType) {
            KeyType.BIOMETRY -> encryptedAccessKeyBiometry = null
            else -> encryptedAccessKeyPin = null
        }
    }

    override fun removeMasterKey(keyType: KeyType) {
        when (keyType) {
            KeyType.BIOMETRY -> encryptedMasterKeyBiometry = null
            else -> encryptedMasterKeyPin = null
        }
    }

    override fun setBiometryEnabled(isEnable: Boolean) {
        isBiometryEnabled = isEnable
    }

    override fun isBiometryEnabled() = isBiometryEnabled ?: false

    override fun addAccessKeyBackup(index: Int, accessKey: ByteArray) {
        accessKeyBackups[index] = accessKey
    }

    override fun getAccessKeyBackup(index: Int) = accessKeyBackups.getOrElse(0) {
        Preferences.main.getAccessKeyBackup(index)
    }

    override fun addKeystoreIvBackup(index: Int, iv: ByteArray) {
        keystoreIvBackups[index] = iv
    }

    override fun getKeystoreIvBackup(index: Int) = keystoreIvBackups.getOrElse(index) {
        Preferences.main.getKeystoreIvBackup(index)
    }

    override fun save() {
        if (!isSaved) {
            keystoreIvBiometry?.let { Preferences.main.setKeystoreIv(KeyType.BIOMETRY, it) }
            keystoreIvPin?.let { Preferences.main.setKeystoreIv(KeyType.PIN, it) }

            encryptedAccessKeyBiometry?.let { Preferences.main.setAccessKey(it, KeyType.BIOMETRY) }
            encryptedAccessKeyPin?.let { Preferences.main.setAccessKey(it, KeyType.PIN) }

            encryptedMasterKeyBiometry?.let { Preferences.main.setMasterKey(it, KeyType.BIOMETRY) }
            encryptedMasterKeyPin?.let { Preferences.main.setMasterKey(it, KeyType.PIN) }

            signatureBiometry?.let { Preferences.main.setSignature(it, KeyType.BIOMETRY) }
            signaturePin?.let { Preferences.main.setSignature(it, KeyType.PIN) }

            biometrySalt?.let { Preferences.main.saveSalt(it) }

            isBiometryEnabled?.let { Preferences.main.setBiometryEnabled(it) }

            for ((index, accessKey) in accessKeyBackups) {
                Preferences.main.setAccessKeyBackup(index, accessKey)
            }

            for ((index, iv) in keystoreIvBackups) {
                Preferences.main.setKeystoreIvBackup(index, iv)
            }

            isSaved = true
        }
    }
}
