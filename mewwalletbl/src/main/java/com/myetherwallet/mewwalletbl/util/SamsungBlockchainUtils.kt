package com.myetherwallet.mewwalletbl.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.data.MessageToSign
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.bip.bip44.Network
import com.myetherwallet.mewwalletkit.core.extension.encode
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import com.myetherwallet.mewwalletkit.eip.eip155.Transaction
import com.myetherwallet.mewwalletkit.eip.eip155.TransactionSignature
import com.samsung.android.sdk.coldwallet.*
import com.samsung.android.sdk.coldwallet.ScwService.ScwSignEthPersonalMessageCallback
import java.math.BigInteger
import java.util.concurrent.CountDownLatch


/**
 * Created by BArtWell on 20.12.2019.
 */

private const val TAG = "SamsungBlockchainUtils"
private const val SDK_MIN_VERSION = 11

object SamsungBlockchainUtils {

    private var cachedAddress: Pair<Address, String>? = null

    private fun getInstance() = ScwService.getInstance()

    fun isAvailable() = getInstance() != null

    fun checkInitialized(): State? {
        val seedHash = try {
            getInstance().seedHash
        } catch (e: Exception) {
            null
        }
        if (seedHash.isNullOrEmpty()) {
            return State.NOT_INITIALIZED
        } else {
            val saved = Preferences.main.getSamsungBlockchainHash()
            return if (saved == null) {
                Preferences.main.setSamsungBlockchainHash(seedHash)
                null
            } else if (saved == seedHash) {
                null
            } else {
                Preferences.main.setSamsungBlockchainHash(null)
                State.RESETED
            }
        }
    }

    fun isSynchronized() = !Preferences.main.getSamsungBlockchainHash().isNullOrEmpty()

    fun reset() {
        resetCachedAddress()
        Preferences.main.setSamsungBlockchainHash(null)
        Preferences.main.setStorageType(null)
        cacheAddress(0)
    }

    private fun resetCachedAddress() {
        MewLog.d(TAG, "Remove cached address")
        cachedAddress = null
    }

    fun getApiLevel() = getInstance().keystoreApiLevel

    fun checkUpdated(callback: () -> Unit) {
        MewLog.d(TAG, "Checking for update")
        val keystoreApiLevel = getApiLevel()
        MewLog.d(TAG, "ApiLevel = $keystoreApiLevel (required $SDK_MIN_VERSION)")
        if (keystoreApiLevel >= SDK_MIN_VERSION) {
            val updateCallback = object : ScwService.ScwCheckForMandatoryAppUpdateCallback() {
                override fun onMandatoryAppUpdateNeeded(needed: Boolean) {
                    if (needed) {
                        MewLog.d(TAG, "Update needed (mandatory update requested). Open Samsung Keystore application and update it")
                        callback()
                    } else {
                        MewLog.d(TAG, "Already updated")
                    }
                }
            }
            getInstance().checkForMandatoryAppUpdate(updateCallback)
        } else {
            MewLog.d(TAG, "Update needed (keystore version less minimal $keystoreApiLevel). Open Samsung Keystore application and update it")
            callback()
        }
    }

    fun startDeepLink(context: Context?, deepLink: DeepLink) {
        MewLog.d(TAG, "startDeepLink: ${deepLink}")
        val uri: Uri = Uri.parse(deepLink.link)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    fun cacheAddress(index: Int) {
        getAddress(index, {
            MewLog.d(TAG, "Cached address with index $index")
            cachedAddress = it
        })
    }

    fun getCachedAddress(): Pair<Address, String>? {
        val address = cachedAddress
        cachedAddress = null
        return address
    }

    fun getAddress(index: Int, successCallback: (Pair<Address, String>?) -> Unit, errorCallback: ((Int, String) -> Unit)? = null) {
        MewLog.d(TAG, "Request addresses")
        if (isAvailable()) {
            MewLog.d(TAG, "Blockchain not available")
            val callback = object : ScwService.ScwGetAddressListCallback() {
                override fun onSuccess(addressList: List<String>) {
                    MewLog.d(TAG, "Addresses: " + addressList.joinToString(" "))
                    val address = addressList.getOrNull(0)?.let { Address.createEthereum(it) }
                    val anonymousId = addressList.getOrNull(1)
                    val result = if (address == null || anonymousId == null) {
                        null
                    } else {
                        Pair(address, anonymousId)
                    }
                    successCallback(result)
                }

                override fun onFailure(errorCode: Int, errorMessage: String?) {
                    MewLog.d(TAG, "Cannot get addresses ($errorCode, ${getErrorName(errorCode)}, $errorMessage)")
                    errorCallback?.invoke(errorCode, errorMessage ?: "Unknown error")
                }
            }
            try {
                getInstance().getAddressList(callback, arrayListOf(ScwService.getHdPath(ScwCoinType.ETH, index), Network.ANONYMIZED_ID.path + "/" + index))
            } catch (e: ScwApiLevelException) {
                MewLog.d(TAG, "Keystore is outdated", e)
                errorCallback?.invoke(-998, "Keystore is outdated")
            }
        }
    }

    fun getAddressSync(index: Int): Pair<Pair<Address, String>?, Pair<Int, String>?> {
        val countDownLatch = CountDownLatch(1)
        var result = Pair<Pair<Address, String>?, Pair<Int, String>?>(null, null)
        getAddress(index, {
            if (it != null) {
                result = Pair(it, null)
            }
            countDownLatch.countDown()
        }, { code, message ->
            result = Pair(null, Pair(code, message))
            countDownLatch.countDown()
        })
        countDownLatch.await()
        return result
    }

    private fun getErrorName(errorCode: Int): String {
        try {
            val fields = ScwErrorCode::class.java.declaredFields
            for (field in fields) {
                if (field.getInt(null) == errorCode) {
                    return field.name
                }
            }
        } catch (e: Exception) {
            MewLog.e(TAG, "Can't parse error", e)
            return "ERROR_RETRIEVE_NAME"
        }
        return "UNKNOWN"
    }

    fun listCoins() {
        MewLog.d(TAG, "Request coins")
        val supportedCoins = getInstance().supportedCoins
        val sb = StringBuilder()
        sb.append("Supported coins: ")
        for (i in supportedCoins.indices) {
            sb.append('[').append(i).append("] ").append(supportedCoins[i]).append(' ')
        }
        val s = sb.toString()
        MewLog.d(TAG, s)
    }

    fun sign(index: Int, transaction: Transaction, callback: (ByteArray?) -> Unit) {
        val signCallback = object : ScwService.ScwSignEthTransactionCallback() {
            override fun onSuccess(signed: ByteArray) {
                MewLog.d(TAG, "Sign success")
                val chainId = transaction.chainId!!
                // Samsung encoding V field incorrectly
                val v = getFixedV(signed, chainId)
                // Length can be changed during fix, so we need to rebuild transaction
                transaction.signature = parseSignature(signed, v, chainId)
                val encoded = transaction.encode()!!
                MewLog.d(TAG, "Signature " + encoded.toHexString())
                callback(encoded)
            }

            override fun onFailure(errorCode: Int, errorMessage: String?) {
                MewLog.d(TAG, "Cannot sign transaction ($errorCode, ${getErrorName(errorCode)}, $errorMessage)")
                callback(null)
            }
        }
        getInstance().signEthTransaction(signCallback, transaction.encode()!!, ScwService.getHdPath(ScwCoinType.ETH, index), transaction.chainId!!.toLong())
    }

    private fun parseSignature(signedTransaction: ByteArray, v: BigInteger, chainId: BigInteger): TransactionSignature {
        val signedTransactionLength = signedTransaction.size
        val rStart = signedTransactionLength - 65
        val r = signedTransaction.copyOfRange(rStart, rStart + 32)
        val s = signedTransaction.copyOfRange(signedTransactionLength - 32, signedTransactionLength)
        return TransactionSignature(r, s, v, chainId)
    }

    fun sign(index: Int, message: MessageToSign, callback: (ByteArray?) -> Unit) {
        val signCallback: ScwSignEthPersonalMessageCallback = object : ScwSignEthPersonalMessageCallback() {
            override fun onSuccess(signed: ByteArray) {
                MewLog.d(TAG, "Sign success")
                callback(signed)
            }

            override fun onFailure(errorCode: Int, errorMessage: String?) {
                MewLog.d(TAG, "Cannot sign message ($errorCode, ${getErrorName(errorCode)}, $errorMessage)")
                callback(null)
            }
        }
        getInstance().signEthPersonalMessage(signCallback, message.text.hexToByteArray(), ScwService.getHdPath(ScwCoinType.ETH, index))
    }

    private fun getFixedV(encoded: ByteArray, chainId: BigInteger): BigInteger {
        val position = encoded.size - 67
        return normalizeV(encoded[position], chainId)
    }

    private fun normalizeV(vByte: Byte, chainId: BigInteger): BigInteger {
        val v = BigInteger(byteArrayOf(vByte))
        return if (vByte == 27.toByte() || vByte == 28.toByte()) {
            v - BigInteger.valueOf(27) + BigInteger.valueOf(35) + BigInteger.valueOf(2) * chainId
        } else {
            v
        }
    }

    enum class DeepLink(val link: String) {
        MAIN(ScwDeepLink.MAIN),
        STORE(ScwDeepLink.GALAXY_STORE),
        RESET(ScwDeepLink.MAIN)
    }

    enum class State(val deepLink: DeepLink) {
        NOT_INITIALIZED(DeepLink.MAIN),
        RESETED(DeepLink.MAIN)
    }
}
