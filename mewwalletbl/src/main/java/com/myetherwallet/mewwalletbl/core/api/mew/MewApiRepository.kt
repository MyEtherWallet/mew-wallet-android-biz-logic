package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.PurchaseHistory
import com.myetherwallet.mewwalletbl.data.PurchaseProviderResponse
import com.myetherwallet.mewwalletbl.data.PurchaseSimplexOrder
import com.myetherwallet.mewwalletbl.data.PurchaseSimplexQuote
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.util.ApplicationUtils
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.*
import retrofit2.Call
import retrofit2.HttpException
import java.math.BigDecimal
import java.security.spec.AlgorithmParameterSpec
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by BArtWell on 21.09.2019.
 */

private const val TAG = "MewApiRepository"

class MewApiRepository(private val service: MewApi) {

    fun getAccountTokens(address: Address): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getAccountTokens(address.address)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTokens(request: RequestByContracts): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTokens(request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getMetaTokens(): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getMetaTokens()) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPrices(request: RequestByContracts): Either<Failure, List<Price>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPrices(request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getBalances(address: Address, limit: Int, request: RequestByContracts): Either<Failure, List<TokenBalance>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getBalances(address.address, limit, request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getBalances(address: Address): Either<Failure, List<TokenBalance>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getBalances(address.address)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransactionHistory(address: String, limit: Int, offset: Int, request: RequestByContracts): Either<Failure, List<Transaction>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTransactionHistory(address, limit, offset, request)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getTransaction(hash: String): Either<Failure, Transaction> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getTransaction(hash)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun sendFcmToken(token: String, addresses: List<String>): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.sendFcmToken(SendFcmTokenRequest(token, addresses))) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPurchaseHistory(id: String, paginationToken: String?): Either<Failure, PurchaseHistory> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPurchaseHistory(hashId(id), paginationToken)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    private fun hashId(id: String): String {
        try {
            if (id.isHex()) {
                return id.hexToByteArray().keccak256().toHexString().addHexPrefix()
            }
        } catch (e: Exception) {
            MewLog.e(TAG, "Cannot hash ID", e)
        }
        return ""
    }

    private fun decryptPurchaseHistory(id: String, input: ByteArray): ByteArray? {
        try {
            val hashedId = id.toLowerCase(Locale.US).toByteArray().keccak256()
            val key: ByteArray = hashedId
            val iv: ByteArray = hashedId.copyOfRange(0, hashedId.size / 2)
            val cipher: Cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
            val secretKeySpec = SecretKeySpec(key, "AES")
            val paramSpec: AlgorithmParameterSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, paramSpec)
            return cipher.doFinal(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getPurchaseSimplexOrder(id: String, paymentId: String, address: String): Either<Failure, PurchaseSimplexOrder> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPurchaseSimplexOrder(ApplicationUtils.getReferenceId(id), paymentId, address)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPurchaseSimplexQuote(id: String, requestedCurrency: String, requestedAmount: BigDecimal): Either<Failure, PurchaseSimplexQuote> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPurchaseSimplexQuote(ApplicationUtils.getReferenceId(id), "USD", requestedCurrency, requestedAmount)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getPurchaseProvider(iso: String): Either<Failure, PurchaseProviderResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getPurchaseProvider(iso)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getMarketPrices(paginationToken: String?): Either<Failure, Market> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getMarketPrices(paginationToken)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun searchMarketPrices(query: String): Either<Failure, List<MarketItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.searchMarketPrices(query)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    fun getMarketInfo(address: String): Either<Failure, MarketItem> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> request(service.getMarketInfo(address)) { it }
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    private fun <T, R> request(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> {
                    val body = response.body()
                    if (body == null) {
                        Either.Left(Failure.ServerError(IllegalStateException("Body is empty")))
                    } else {
                        Either.Right(transform(body))
                    }
                }
                false -> Either.Left(Failure.ServerError(HttpException(response)))
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.UnknownError(exception))
        }
    }
}
