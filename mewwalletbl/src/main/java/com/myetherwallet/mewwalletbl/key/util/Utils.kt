package com.myetherwallet.mewwalletbl.key.util

import java.security.SecureRandom

/**
 * Created by BArtWell on 17.07.2019.
 */

internal object Utils {

    fun getRandomBytes(size: Int): ByteArray {
        val bytes = ByteArray(size)
        SecureRandom().nextBytes(bytes)
        return bytes
    }
}