package com.myetherwallet.mewwalletbl.connection

import com.myetherwallet.mewwalletbl.MewEnvironment
import com.myetherwallet.mewwalletbl.data.BaseMessage
import com.myetherwallet.mewwalletbl.data.EncryptedMessage
import com.myetherwallet.mewwalletkit.bip.bip44.PrivateKey
import com.myetherwallet.mewwalletkit.core.extension.*
import com.myetherwallet.mewwalletkit.core.util.HMAC
import org.spongycastle.crypto.engines.AESEngine
import org.spongycastle.crypto.modes.CBCBlockCipher
import org.spongycastle.crypto.paddings.PaddedBufferedBlockCipher
import org.spongycastle.crypto.params.KeyParameter
import org.spongycastle.crypto.params.ParametersWithIV
import org.spongycastle.jce.ECNamedCurveTable
import org.spongycastle.jce.provider.BouncyCastleProvider
import org.spongycastle.jce.spec.ECPrivateKeySpec
import org.spongycastle.jce.spec.ECPublicKeySpec
import java.math.BigInteger
import java.security.KeyFactory
import java.security.SecureRandom
import java.security.Security
import java.util.*
import javax.crypto.KeyAgreement

/**
 * Created by BArtWell on 24.07.2019.
 */

class MessageCrypt(private val privateKey: String) {

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    fun signMessage(data: String): String {
        val hash = data.hashPersonalMessage()
        val signature = hash.secp256k1RecoverableSign(this.privateKey.hexToByteArray())
        val serialized = signature?.secp256k1SerializeSignature()
        val cropped = serialized!!.copyOfRange(0, 64)
        return (byteArrayOf((serialized[64].toInt() + 27).toByte()) + cropped).toHexString()
    }

    fun encrypt(data: BaseMessage) = encrypt(data.toByteArray())

    fun encrypt(data: ByteArray): EncryptedMessage {
        val connectPublicKey = publicKeyFromPrivateWithControl(privateKey)
        val initVector = SecureRandom().generateSeed(16)

        val ephemPrivateKey = SecureRandom().generateSeed(32).toHexString().lowercase(Locale.getDefault())
        val ephemPublicKey = publicKeyFromPrivateWithControl(ephemPrivateKey)

        val multipliedKeys = multiplyKeys(ephemPrivateKey, connectPublicKey)
        val hashed = multipliedKeys.sha512()

        val encKey = Arrays.copyOfRange(hashed, 0, 32)
        val macKey = Arrays.copyOfRange(hashed, 32, hashed.size)

        val cipher = encryptAes256Cbc(initVector, encKey, data)

        val dataToHMac = initVector + ephemPublicKey + cipher
        val macData = HMAC.authenticate(macKey, HMAC.Algorithm.HmacSHA256, dataToHMac)

        return EncryptedMessage(cipher, ephemPublicKey, initVector, macData)
    }

    fun decrypt(message: EncryptedMessage): ByteArray? {
        val multipliedKeys = multiplyKeys(privateKey, message.ephemPublicKey.data)
        val hashed = multipliedKeys.sha512()

        val encKey = Arrays.copyOfRange(hashed, 0, 32)
        val macKey = Arrays.copyOfRange(hashed, 32, hashed.size)

        val dataToHMac = message.iv.data + message.ephemPublicKey.data + message.ciphertext.data
        val macData = HMAC.authenticate(macKey, HMAC.Algorithm.HmacSHA256, dataToHMac)

        if (!Arrays.equals(macData, message.mac.data)) {
            return null
        }

        return decryptAes256Cbc(encKey, message.ciphertext.data, message.iv.data)
    }

    private fun publicKeyFromPrivateWithControl(privateKeySource: String): ByteArray {
        val privateKey = PrivateKey.createWithPrivateKey(privateKeySource.hexToByteArray(), MewEnvironment.current.network)
        val publicKey = privateKey.publicKey()
        return publicKey!!.data()
    }

    private fun multiplyKeys(privateKey: String, publicKey: ByteArray): ByteArray {
        val keyAgreement = KeyAgreement.getInstance("ECDH", "SC")
        val keyFactory = KeyFactory.getInstance("ECDH", "SC")
        val params = ECNamedCurveTable.getParameterSpec("secp256k1")
        val privateKeyItem = keyFactory.generatePrivate(ECPrivateKeySpec(BigInteger(privateKey, 16), params))
        keyAgreement.init(privateKeyItem)
        val publicKeyItem = keyFactory.generatePublic(ECPublicKeySpec(params.curve.decodePoint(publicKey), params))
        keyAgreement.doPhase(publicKeyItem, true)
        return keyAgreement.generateSecret()
    }

    private fun encryptAes256Cbc(initVector: ByteArray, key: ByteArray, data: ByteArray): ByteArray {
        try {
            val cipher = PaddedBufferedBlockCipher(CBCBlockCipher(AESEngine()))

            cipher.init(true, ParametersWithIV(KeyParameter(key), initVector))
            val out = ByteArray(cipher.getOutputSize(data.size))

            val processed = cipher.processBytes(data, 0, data.size, out, 0)
            cipher.doFinal(out, processed)

            return out
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ByteArray(0)
    }

    private fun decryptAes256Cbc(encKey: ByteArray, cipher: ByteArray, initVector: ByteArray): ByteArray? {
        try {
            val blockCipher = PaddedBufferedBlockCipher(CBCBlockCipher(AESEngine()))
            blockCipher.init(false, ParametersWithIV(KeyParameter(encKey), initVector))
            val buffer = ByteArray(blockCipher.getOutputSize(cipher.size))
            var len = blockCipher.processBytes(cipher, 0, cipher.size, buffer, 0)
            len += blockCipher.doFinal(buffer, len)
            return Arrays.copyOfRange(buffer, 0, len)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}
