package com.myetherwallet.mewwalletbl.key

import android.content.Context
import com.myetherwallet.mewwalletbl.key.storage.EncryptStorage
import com.myetherwallet.mewwalletbl.key.storage.PersistentEncryptStorage
import com.myetherwallet.mewwalletbl.key.util.AES
import com.myetherwallet.mewwalletbl.key.util.HKDF
import com.myetherwallet.mewwalletbl.key.util.KeyIntegrityUtils
import com.myetherwallet.mewwalletbl.key.util.Utils
import com.myetherwallet.mewwalletkit.core.extension.md5
import com.myetherwallet.mewwalletkit.core.extension.sha512

/**
 * Created by BArtWell on 05.08.2019.
 */

class PinEncryptHelper(context: Context, private val pinString: String, encryptStorage: EncryptStorage = PersistentEncryptStorage()) : BaseEncryptHelper(context, KeyType.PIN, encryptStorage) {

    private val pin = pinString.toByteArray()
    private var wasAccessKeyGenerated = false

    constructor(encryptHelper: PinEncryptHelper, encryptStorage: EncryptStorage = encryptHelper.encryptStorage) : this(encryptHelper.context, encryptHelper.pinString, encryptStorage)

    init {
        if (!encryptStorage.isAccessKeyExists(KeyType.PIN)) {
            createAccessAndMasterKeys()
        }
    }

    fun checkPin(): Boolean {
        val signature = encryptStorage.getSignature(KeyType.PIN)
        return signature != null && signPin().contentEquals(signature)
    }

    override fun retrieveAccessKey(): ByteArray {
        if (!wasAccessKeyGenerated && !checkPin()) {
            throw IllegalStateException("You have to check PIN first")
        }
        return super.retrieveAccessKey()
    }

    private fun signPin() = pin.sha512().md5()

    private fun createAccessAndMasterKeys() {
        val accessKey = generateAccessKey(pin)
        val masterKey = generateMasterKey()
        val encryptedAccessKey = keystoreHelper.encrypt(accessKey)
        val encryptedMasterKey = AES.encrypt(masterKey, accessKey)
        encryptStorage.setAccessKey(encryptedAccessKey, KeyType.PIN)
        encryptStorage.setMasterKey(encryptedMasterKey, KeyType.PIN)
        encryptStorage.setSignature(signPin(), KeyType.PIN)

        KeyIntegrityUtils.saveBackupKeys(context, encryptStorage, accessKey, true)
    }

    private fun generateMasterKey(): ByteArray {
        val masterPassword = Utils.getRandomBytes(64)
        val masterSalt = Utils.getRandomBytes(64)
        return HKDF.calculate(masterPassword, masterSalt)
    }

    override fun generateAccessKey(salt: ByteArray): ByteArray {
        wasAccessKeyGenerated = true
        return super.generateAccessKey(salt)
    }
}
