package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.data.api.binance.*
import com.myetherwallet.mewwalletbl.data.api.market.MarketCollectionItem
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem
import com.myetherwallet.mewwalletbl.data.dex.DexPriceResult
import com.myetherwallet.mewwalletbl.data.dex.DexToken
import com.myetherwallet.mewwalletbl.data.dex.DexTradeResult
import com.myetherwallet.mewwalletbl.data.staked.*
import com.myetherwallet.mewwalletbl.util.ApplicationUtils
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.*
import okio.IOException
import retrofit2.Call
import retrofit2.HttpException
import java.math.BigDecimal
import java.security.spec.AlgorithmParameterSpec
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by BArtWell on 21.09.2019.
 */

private const val TAG = "MewApiRepository"

class MewApiRepository(private val service: MewApi) {

    suspend fun getTokens(request: RequestByContracts): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getTokens(request) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMetaTokens(): Either<Failure, List<Token>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMetaTokens() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getPrices(request: RequestByContracts): Either<Failure, List<SimplePrice>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPrices(request) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBalances(address: Address): Either<Failure, List<TokenBalance>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBalances(address.address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getTransactionHistory(address: String, request: RequestByContracts): Either<Failure, List<Transaction>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getTransactionHistory(address, request) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getTransaction(hash: String): Either<Failure, Transaction> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getTransaction(hash) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun sendFcmToken(token: String, addresses: List<String>): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.sendFcmToken(SendFcmTokenRequest(token, addresses)) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun sendSurvey(email: String, message: String, rating: Int): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.sendSurvey(SendSurveyRequest(email, message, rating)) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getPurchaseHistory(id: String, paginationToken: String?): Either<Failure, PurchaseHistory> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPurchaseHistory(hashId(id), paginationToken) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getAccountTokens(address: Address): Either<Failure, List<Price>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPrices(address.address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    private suspend fun hashId(id: String): String {
        try {
            if (id.isHex()) {
                return id.hexToByteArray().keccak256().toHexString().addHexPrefix()
            }
        } catch (e: Exception) {
            MewLog.e(TAG, "Cannot hash ID", e)
        }
        return ""
    }

    private suspend fun decryptPurchaseHistory(id: String, input: ByteArray): ByteArray? {
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

    suspend fun getPurchaseSimplexQuote(id: String, requestedCurrency: String, requestedAmount: BigDecimal): Either<Failure, PurchaseSimplexQuote> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPurchaseSimplexQuote(ApplicationUtils.getReferenceId(id), requestedCurrency, requestedCurrency, requestedAmount) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getPurchaseProvider(iso: String): Either<Failure, List<PurchaseProvider>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPurchaseProviders(iso) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMarketOverview(period: String?): Either<Failure, MarketOverviewResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketOverview(period) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMarketMovers(): Either<Failure, List<MarketItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketMovers() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMarketCollections(): Either<Failure, List<MarketCollectionItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketCollections() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMarketPrices(paginationToken: Int): Either<Failure, MarketResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketPrices(paginationToken) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun searchMarketPrices(query: String): Either<Failure, List<MarketItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.searchMarketPrices(query) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMarketInfo(address: String): Either<Failure, MarketItem> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketInfo(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getPriceHistory(address: String, from: Date, to: Date): Either<Failure, MarketPriceVolumeHistory> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPriceHistory(address, from.time / 1000, to.time / 1000) }) {
                MarketPriceVolumeHistory(
                    it.prices.map { item ->
                        PriceHistoryItem(dateFormat.parse(item[0])!!, item[1].toBigDecimal())
                    },
                    it.volumes.map { item ->
                        VolumeHistoryItem(dateFormat.parse(item[0])!!, item[1].toBigDecimal())
                    }
                )
            }
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getSwapList(): Either<Failure, List<DexToken>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSwapList() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getSwapPrice(fromContract: String, toContract: String, amount: String, includeFees: Boolean): Either<Failure, DexPriceResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSwapPrice(fromContract, toContract, amount, includeFees) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getSwapTrade(address: String, dex: String, exchange: String, fromContract: String, toContract: String, amount: String): Either<Failure, DexTradeResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSwapTrade(address, dex, exchange, fromContract, toContract, amount, "android") }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceList(): Either<Failure, List<BinanceToken>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceList() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceNetworks(symbol: String): Either<Failure, List<BinanceNetwork>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceNetworks(symbol) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun createBinanceSwap(
        amount: String,
        symbol: String,
        fromAddress: String?,
        toAddress: String,
        fromNetwork: String,
        toNetwork: String,
        toAmount: String?,
        exchangeGasAmount: String?
    ): Either<Failure, BinanceStatus> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.createBinanceSwap(amount, symbol, fromAddress, toAddress, fromNetwork, toNetwork, toAmount, exchangeGasAmount) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceTransaction(address: String, id: String): Either<Failure, List<BinanceTransaction>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceTransaction(address, id) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceStatus(address: String, id: String): Either<Failure, BinanceStatus> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceStatus(address, id) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceHistory(address: String): Either<Failure, List<BinanceStatus>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceHistory(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceQuota(address: String): Either<Failure, BinanceQuota> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceQuota(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getStakedInfo(): Either<Failure, StakedInfo> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getStakedInfo() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getStakedTransactions(eth2Address: String, amount: Int): Either<Failure, List<StakedTransactions>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getStakedTransactions(eth2Address, amount) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getStakedHistory(address: String): Either<Failure, List<StakedHistoryInfo>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getStakedHistory(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getStakedStatus(address: String, uuid: String): Either<Failure, StakedHistoryInfo> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getStakedStatus(address, uuid) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getStakedTransaction(address: String, uuid: String): Either<Failure, StakedTransaction> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getStakedTransaction(address, uuid) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getStakedProvision(address: String, eth2Address: String, amount: Int): Either<Failure, StakedProvisionResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getStakedProvision(address, eth2Address, amount) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun submitTransaction(request: StakedSubmitTransactionRequest): Either<Failure, StakedSubmitTransactionResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.submitTransaction(request) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getEstimateTransactionSpeed(request: RequestEstimateByPrice): Either<Failure, List<EstimateTransactionSpeed>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getEstimateTransactionSpeed(request) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getIntercomHash(id: String, iso: String): Either<Failure, String> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getIntercomHash(id, iso) }, { it.result })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    private suspend fun <T, R> request(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
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
                false -> {
                    val bodyError = response.errorBody()
                    if (bodyError == null) {
                        Either.Left(Failure.ServerError(HttpException(response)))
                    } else {
                        Either.Left(Failure.CommonError(response.code(), bodyError.string()))
                    }
                }
            }
        } catch (exception: Throwable) {
            exception.printStackTrace()
            Either.Left(Failure.UnknownError(exception))
        }
    }

    private suspend fun <T, R> requestSuspend(call: suspend () -> T, transform: (T) -> R): Either<Failure, R> {
        return try {
            val response = call()
            Either.Right(transform(response))
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            when (throwable) {
                is IOException -> Either.Left(Failure.ServerError(IllegalStateException("Network error")))
                is HttpException -> {
                    Either.Left(Failure.CommonError(throwable.code(), throwable.message()))
                }
            }
            Either.Left(Failure.UnknownError(throwable))
        }
    }

    suspend fun getTransactionType(address: Address): Either<Failure, AddressTypeInfo> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getAddressTypeInfo(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getExchangeRates(): Either<Failure, List<ExchangeRates>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getExchangeRates() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }
}
