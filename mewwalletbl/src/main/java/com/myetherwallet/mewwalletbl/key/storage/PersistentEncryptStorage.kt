package com.myetherwallet.mewwalletbl.key.storage

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.preference.Preferences

class PersistentEncryptStorage : EncryptStorage {

    override fun setKeystoreIv(keyType: KeyType, iv: ByteArray) {
        Preferences.main.setKeystoreIv(keyType, iv)
    }

    override fun getKeystoreIv(keyType: KeyType) = Preferences.main.getKeystoreIv(keyType)

    override fun getAccessKey(keyType: KeyType) = Preferences.main.getAccessKey(keyType)

    override fun getMasterKey(keyType: KeyType) = Preferences.main.getMasterKey(keyType)

    override fun isAccessKeyExists(keyType: KeyType) = Preferences.main.isAccessKeyExists(keyType)

    override fun getSignature(keyType: KeyType) = Preferences.main.getSignature(keyType)

    override fun setAccessKey(key: ByteArray, keyType: KeyType) {
        Preferences.main.setAccessKey(key, keyType)
    }

    override fun setMasterKey(key: ByteArray, keyType: KeyType) {
        Preferences.main.setMasterKey(key, keyType)
    }

    override fun setSignature(key: ByteArray, keyType: KeyType) {
        Preferences.main.setSignature(key, keyType)
    }

    override fun getBiometrySalt() = Preferences.main.getBiometrySalt()

    override fun removeBiometrySalt() {
        Preferences.main.removeBiometrySalt()
    }

    override fun removeAccessKey(keyType: KeyType) {
        Preferences.main.removeAccessKey(keyType)
    }

    override fun removeMasterKey(keyType: KeyType) {
        Preferences.main.removeMasterKey(keyType)
    }

    override fun setBiometryEnabled(isEnable: Boolean) {
        Preferences.main.setBiometryEnabled(isEnable)
    }

    override fun isBiometryEnabled() = Preferences.main.isBiometryEnabled()

    override fun addAccessKeyBackup(index: Int, accessKey: ByteArray) = Preferences.main.setAccessKeyBackup(index, accessKey)

    override fun getAccessKeyBackup(index: Int) = Preferences.main.getAccessKeyBackup(index)

    override fun addKeystoreIvBackup(index: Int, iv: ByteArray) = Preferences.main.setKeystoreIvBackup(index, iv)

    override fun getKeystoreIvBackup(index: Int) = Preferences.main.getKeystoreIvBackup(index)

    override fun save() {
        MewLog.d("PersistentEncryptStorage", "save")
    }
}
