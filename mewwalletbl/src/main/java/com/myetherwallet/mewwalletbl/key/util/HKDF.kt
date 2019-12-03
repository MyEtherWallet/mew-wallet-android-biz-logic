package com.myetherwallet.mewwalletbl.key.util

import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


/**
 * Created by BArtWell on 12.07.2019.
 */

internal object HKDF {
    fun calculate(password: ByteArray, salt: ByteArray): ByteArray {
        try {
            val mac = Mac.getInstance("HmacSHA256")
            mac.init(SecretKeySpec(salt, "HmacSHA256"))
            return mac.doFinal(password)
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError(e)
        } catch (e: InvalidKeyException) {
            throw AssertionError(e)
        }
    }
}
