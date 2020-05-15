package com.myetherwallet.mewwalletbl.key

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.key.storage.EncryptStorage
import com.myetherwallet.mewwalletbl.key.storage.PersistentEncryptStorage
import com.myetherwallet.mewwalletbl.key.util.AES
import javax.crypto.Cipher

/**
 * Created by BArtWell on 05.08.2019.
 */

private const val TAG = "BiometryEncryptHelper"

class BiometryEncryptHelper(context: Context, encryptStorage: EncryptStorage = PersistentEncryptStorage()) : BaseEncryptHelper(context, KeyType.BIOMETRY, encryptStorage) {

    fun prepareForInit(biometryCallback: (Cipher, (Cipher) -> Unit) -> Unit, callback: () -> Unit) =
        prepare(true, { cipher, function -> biometryCallback(cipher!!, function) }, callback)

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

    fun init(pin: String) = init(PinEncryptHelper(context, pin, encryptStorage))

    fun init(encryptHelper: BaseEncryptHelper) {
        assertPrepared()
        val masterKey = encryptHelper.retrieveMasterKey(encryptHelper.accessKey)
        encryptHelper.destroy()
        val biometryAccessKey = generateAccessKey(encryptStorage.getBiometrySalt())
        val encryptedAccessKey = keystoreHelper.encrypt(biometryAccessKey)
        val encryptedMasterKey = AES.encrypt(masterKey, biometryAccessKey)
        encryptStorage.setAccessKey(encryptedAccessKey, KeyType.BIOMETRY)
        encryptStorage.setMasterKey(encryptedMasterKey, KeyType.BIOMETRY)
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
        encryptStorage.removeBiometrySalt()
        encryptStorage.removeAccessKey(KeyType.BIOMETRY)
        encryptStorage.removeMasterKey(KeyType.BIOMETRY)
    }
}
