package com.myetherwallet.mewwalletbl.key.util

import android.security.keystore.KeyProperties
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec


/**
 * Created by BArtWell on 15.07.2019.
 */

private const val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
private const val TRANSFORMATION = "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_ECB}/${KeyProperties.ENCRYPTION_PADDING_PKCS7}"

internal object AES {

    fun encrypt(input: ByteArray, key: ByteArray) = crypt(input, key, Cipher.ENCRYPT_MODE)

    fun decrypt(input: ByteArray, key: ByteArray) = crypt(input, key, Cipher.DECRYPT_MODE)

    private fun crypt(input: ByteArray, key: ByteArray, mode: Int): ByteArray {
        val secretKeySpec = SecretKeySpec(key, ALGORITHM)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(mode, secretKeySpec)
        return cipher.doFinal(input)
    }
}