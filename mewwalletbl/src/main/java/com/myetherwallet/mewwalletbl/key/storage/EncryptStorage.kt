package com.myetherwallet.mewwalletbl.key.storage

import com.myetherwallet.mewwalletbl.key.KeyType

interface EncryptStorage {

    fun setKeystoreIv(keyType: KeyType, iv: ByteArray)
    fun getKeystoreIv(keyType: KeyType): ByteArray

    fun setAccessKey(key: ByteArray, keyType: KeyType)
    fun getAccessKey(keyType: KeyType) : ByteArray
    fun isAccessKeyExists(keyType: KeyType) : Boolean

    fun setMasterKey(key: ByteArray, keyType: KeyType)
    fun getMasterKey(keyType: KeyType) : ByteArray

    fun setSignature(key: ByteArray, keyType: KeyType)
    fun getSignature(keyType: KeyType) : ByteArray?

    fun getBiometrySalt(): ByteArray

    fun removeBiometrySalt()
    fun removeAccessKey(keyType: KeyType)
    fun removeMasterKey(keyType: KeyType)

    fun setBiometryEnabled(isEnable: Boolean)
    fun isBiometryEnabled() : Boolean

    fun save()
}