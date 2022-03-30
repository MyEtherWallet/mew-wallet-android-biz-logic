package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import android.util.Base64
import java.util.*

private const val PREFERENCES_NAME = "wallet"
private const val MNEMONIC = "mnemonic"
private const val PREFIX_PRIVATE_KEY = "private_key_"
private const val BACKUP = "backup"

class WalletPreferences internal constructor(context: Context) : BasePreferences(context, PREFERENCES_NAME) {

    fun setPrivateKey(address: String, privateKey: ByteArray) {
        setString(PREFIX_PRIVATE_KEY + address, Base64.encodeToString(privateKey, Base64.DEFAULT))
    }

    fun getPrivateKey(address: String): ByteArray? {
        for ((key, value) in getAll()) {
            if (key.lowercase(Locale.US) == (PREFIX_PRIVATE_KEY + address).lowercase(Locale.US)) {
                return (value as String?)?.let {
                    Base64.decode(it, Base64.DEFAULT)
                }
            }
        }
        return null
    }

    fun setMnemonic(mnemonic: ByteArray) {
        setString(MNEMONIC, Base64.encodeToString(mnemonic, Base64.DEFAULT))
    }

    fun getMnemonic() = getStringOrNull(MNEMONIC)?.let {
        Base64.decode(it, Base64.DEFAULT)
    }

    fun getWalletAddresses(): List<String> {
        val addresses = mutableListOf<String>()
        for ((address, _) in getAll().filterKeys { it.startsWith(PREFIX_PRIVATE_KEY) }) {
            addresses.add(address.removePrefix(PREFIX_PRIVATE_KEY))
        }
        return addresses
    }

    fun setBackedUp(isBackedUp: Boolean) = setBoolean(BACKUP, isBackedUp)

    fun isBackedUp() = getBoolean(BACKUP, false)

    fun removeAllData() = clear()
}
