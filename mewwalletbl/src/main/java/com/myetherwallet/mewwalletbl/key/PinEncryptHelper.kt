package com.myetherwallet.mewwalletbl.key

import android.content.Context
import com.myetherwallet.mewwalletbl.key.util.AES
import com.myetherwallet.mewwalletbl.key.util.HKDF
import com.myetherwallet.mewwalletbl.key.util.Utils
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletkit.core.extension.md5
import com.myetherwallet.mewwalletkit.core.extension.sha512

/**
 * Created by BArtWell on 05.08.2019.
 */

class PinEncryptHelper(context: Context, pinString: String) : BaseEncryptHelper(context, KeyType.PIN) {

    private val pin = pinString.toByteArray()
    private var wasAccessKeyGenerated = false

    init {
        if (!Preferences.main.isAccessKeyExists(KeyType.PIN)) {
            createAccessAndMasterKeys()
        }
    }

    fun checkPin(): Boolean {
        val signature = Preferences.main.getSignature(KeyType.PIN)
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
        Preferences.main.setAccessKey(encryptedAccessKey, KeyType.PIN)
        Preferences.main.setMasterKey(encryptedMasterKey, KeyType.PIN)
        Preferences.main.setSignature(signPin(), KeyType.PIN)
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
