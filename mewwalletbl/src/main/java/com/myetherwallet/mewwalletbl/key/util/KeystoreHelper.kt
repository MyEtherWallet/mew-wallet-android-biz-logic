package com.myetherwallet.mewwalletbl.key.util

import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import com.myetherwallet.mewwalletbl.key.KeyType
import com.myetherwallet.mewwalletbl.key.storage.EncryptStorage
import okhttp3.internal.closeQuietly
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.security.KeyStore
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.KeyGenerator
import javax.crypto.spec.GCMParameterSpec

/**
 * Created by BArtWell on 12.07.2019.
 */

private const val PROVIDER_ANDROID_KEYSTORE = "AndroidKeyStore"
private const val TRANSFORMATION = "${KeyProperties.KEY_ALGORITHM_AES}/${KeyProperties.BLOCK_MODE_GCM}/${KeyProperties.ENCRYPTION_PADDING_NONE}"

class KeystoreHelper(private val context: Context, private val keyType: KeyType, private val encryptStorage: EncryptStorage) {

    private val alias = "MewWallet" + keyType.name.toLowerCase().capitalize() + "Key"
    private var keyStore: KeyStore = KeyStore.getInstance(PROVIDER_ANDROID_KEYSTORE)
    var signedCipher: Cipher? = null

    init {
        keyStore.load(null)
        if (!isAliasExists()) {
            internalCreateKeys()
        }
    }

    private fun isAliasExists() = Collections.list(keyStore.aliases()).contains(alias)

    private fun internalCreateKeys() {
        var initialLocale: Locale? = null
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            initialLocale = Locale.getDefault()
            setLocale(Locale.ENGLISH)
        }

        val isBiometry = keyType == KeyType.BIOMETRY
        val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, PROVIDER_ANDROID_KEYSTORE)

        val builder = KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT).apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setUserAuthenticationRequired(isBiometry)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setInvalidatedByBiometricEnrollment(isBiometry)
            }
        }
        keyGenerator.init(builder.build())
        keyGenerator.generateKey()

        if (initialLocale != null) {
            setLocale(initialLocale)
        }
    }

    fun getEncryptCipher(): Cipher {
        val key = keyStore.getKey(alias, null)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        encryptStorage.setKeystoreIv(keyType, cipher.iv)
        return cipher
    }

    fun encrypt(data: ByteArray): ByteArray {
        val cipher = signedCipher ?: getEncryptCipher()
        val outputStream = ByteArrayOutputStream()
        val cipherOutputStream = CipherOutputStream(outputStream, cipher)
        cipherOutputStream.write(data)
        cipherOutputStream.close()
        signedCipher = null
        return outputStream.toByteArray()
    }

    fun getDecryptCipher(): Cipher {
        val key = keyStore.getKey(alias, null)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        val iv = encryptStorage.getKeystoreIv(keyType)
        val ivParameterSpec = GCMParameterSpec(128, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec)
        return cipher
    }

    fun decrypt(input: ByteArray): ByteArray {
        var result = ByteArray(0)
        val cipher = signedCipher ?: getDecryptCipher()
        var byteArrayInputStream: ByteArrayInputStream? = null
        try {
            byteArrayInputStream = ByteArrayInputStream(input)
            var cipherInputStream: CipherInputStream? = null
            try {
                cipherInputStream = CipherInputStream(byteArrayInputStream, cipher)
                var byteArrayOutputStream: ByteArrayOutputStream? = null
                try {
                    byteArrayOutputStream = ByteArrayOutputStream()
                    val buffer = ByteArray(1024)
                    var numberOfBytesRead: Int
                    while (cipherInputStream.read(buffer).also { numberOfBytesRead = it } >= 0) {
                        byteArrayOutputStream.write(buffer, 0, numberOfBytesRead)
                    }
                    result = byteArrayOutputStream.toByteArray()
                } finally {
                    byteArrayOutputStream?.closeQuietly()
                }
            } finally {
                cipherInputStream?.closeQuietly()
            }
        } finally {
            byteArrayInputStream?.closeQuietly()
        }
        signedCipher = null
        return result
    }

    fun remove() {
        keyStore.deleteEntry(alias)
    }

    // Known issue: https://issuetracker.google.com/issues/37095309 (crash with Persian language)
    // Force english locale
    private fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}