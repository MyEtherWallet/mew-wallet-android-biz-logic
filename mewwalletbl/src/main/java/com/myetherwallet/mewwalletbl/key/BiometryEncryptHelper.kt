package com.myetherwallet.mewwalletbl.key

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.key.util.AES
import com.myetherwallet.mewwalletbl.preference.Preferences
import javax.crypto.Cipher

/**
 * Created by BArtWell on 05.08.2019.
 */

private const val TAG = "BiometryEncryptHelper"

class BiometryEncryptHelper(context: Context) : BaseEncryptHelper(context, KeyType.BIOMETRY) {

    fun prepareForInit(biometryCallback: (Cipher, (Cipher) -> Unit) -> Unit, callback: () -> Unit) = prepare(true, { cipher, function -> biometryCallback(cipher!!, function) }, callback)

    fun prepareForUse(biometryCallback: (Cipher?, (Cipher) -> Unit) -> Unit, callback: () -> Unit) = prepare(false, biometryCallback, callback)

    private fun prepare(forInit: Boolean, biometryCallback: (Cipher?, (Cipher) -> Unit) -> Unit, callback: () -> Unit) {
        var cipher: Cipher? = null
        try {
            cipher = retrieveCipher(forInit)
        } catch (e: KeyPermanentlyInvalidatedException) {
            MewLog.d(TAG, "Biometry data was changed", e)
            onKeyPermanentlyInvalidated()
        }
        if (cipher == null) {
            biometryCallback.invoke(null) {}
            destroy()
        } else {
            biometryCallback.invoke(cipher) {
                keystoreHelper.signedCipher = it
                callback()
            }
        }
    }

    private fun retrieveCipher(willEncrypt: Boolean) = if (willEncrypt) {
        keystoreHelper.getEncryptCipher()
    } else {
        keystoreHelper.getDecryptCipher()
    }

    private fun isPrepared() = keystoreHelper.signedCipher != null

    fun init(pin: String) {
        assertPrepared()
        val pinEncryptHelper = PinEncryptHelper(context, pin)
        val masterKey = pinEncryptHelper.retrieveMasterKey(pinEncryptHelper.accessKey)
        pinEncryptHelper.destroy()
        val biometryAccessKey = generateAccessKey(Preferences.main.getBiometrySalt())
        val encryptedAccessKey = keystoreHelper.encrypt(biometryAccessKey)
        val encryptedMasterKey = AES.encrypt(masterKey, biometryAccessKey)
        Preferences.main.setAccessKey(encryptedAccessKey, KeyType.BIOMETRY)
        Preferences.main.setMasterKey(encryptedMasterKey, KeyType.BIOMETRY)
    }

    override fun retrieveAccessKey(): ByteArray {
        assertPrepared()
        return super.retrieveAccessKey()
    }

    private fun assertPrepared() {
        if (!isPrepared()) {
            throw IllegalStateException("You have to call prepare() method first")
        }
    }

    private fun onKeyPermanentlyInvalidated() {
        keystoreHelper.remove()
        Preferences.main.removeBiometrySalt()
        Preferences.main.removeAccessKey(KeyType.BIOMETRY)
        Preferences.main.removeMasterKey(KeyType.BIOMETRY)
    }
}
