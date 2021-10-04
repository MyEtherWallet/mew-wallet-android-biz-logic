package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.data.api.binance.*
import com.myetherwallet.mewwalletbl.data.api.market.MarketCollectionItem
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnBalance
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnDepositResult
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnHistoryItem
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnInfo
import com.myetherwallet.mewwalletbl.data.dex.DexPriceResult
import com.myetherwallet.mewwalletbl.data.dex.DexTradeResult
import com.myetherwallet.mewwalletbl.data.staked.*
import com.myetherwallet.mewwalletbl.data.ws.GetIntercomHashResponse
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.*
import okio.IOException
import retrofit2.Call
import retrofit2.HttpException
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

    suspend fun getBalances(blockchain: Blockchain, address: Address): Either<Failure, List<TokenBalance>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBalances(blockchain.symbol, address.address) }, { it })
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

    suspend fun getSurveyUrl(iso: String): Either<Failure, String> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSurveyUrl(iso) }, { it.url })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getPurchaseHistory(id: String, paginationToken: String?): Either<Failure, PurchaseHistory> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPurchaseHistory(hashId(id), paginationToken) }, { it })
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
            val hashedId = id.lowercase(Locale.US).toByteArray().keccak256()
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

    suspend fun getPurchaseProvider(cryptoCurrency: String, iso: String): Either<Failure, List<PurchaseProvider>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPurchaseProviders(cryptoCurrency, iso) }, { it })
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

    suspend fun getMarketPrices(blockchain: Blockchain, paginationToken: Int): Either<Failure, MarketResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketPrices(blockchain.symbol, paginationToken) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun searchMarketPrices(blockchain: Blockchain, query: String): Either<Failure, List<MarketItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.searchMarketPrices(blockchain.symbol, query) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getMarketInfo(blockchain: Blockchain, address: String): Either<Failure, MarketItem> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getMarketInfo(blockchain.symbol, address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getPriceHistory(blockchain: Blockchain, address: String, from: Date, to: Date): Either<Failure, MarketPriceVolumeHistory> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getPriceHistory(blockchain.symbol, address, from.time / 1000, to.time / 1000) }) {
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

    suspend fun getSwapList(blockchain: Blockchain): Either<Failure, List<MarketItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSwapList(blockchain.symbol) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getSwapPrice(blockchain: Blockchain, fromContract: String, toContract: String, amount: String, includeFees: Boolean): Either<Failure, DexPriceResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSwapPrice(blockchain.symbol, fromContract, toContract, amount, includeFees) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getSwapTrade(blockchain: Blockchain, address: String, dex: String, exchange: String, fromContract: String, toContract: String, amount: String): Either<Failure, DexTradeResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getSwapTrade(blockchain.symbol, address, dex, exchange, fromContract, toContract, amount, "android") }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceList(iso: String): Either<Failure, List<BinanceToken>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceList(iso) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceNetworks(iso: String, symbol: String): Either<Failure, List<BinanceNetwork>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceNetworks(iso, symbol) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getYearnDeposit(address: Address, amount: String, token: String): Either<Failure, YearnDepositResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getYearnDeposit(address, amount, token) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getYearnWithdraw(address: Address, amount: String, token: String): Either<Failure, YearnDepositResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getYearnWithdraw(address, amount, token) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getYearnBalance(address: Address): Either<Failure, List<YearnBalance>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getYearnBalance(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getYearnInfo(): Either<Failure, List<YearnInfo>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getYearnInfo() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getYearnHistory(address: Address): Either<Failure, List<YearnHistoryItem>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getYearnHistory(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun createBinanceSwap(
        iso: String,
        amount: String,
        symbol: String,
        fromAddress: String?,
        toAddress: String,
        fromNetwork: String,
        toNetwork: String,
        exchangeGasAmount: String?,
        toAmount: String?
    ): Either<Failure, BinanceStatus> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.createBinanceSwap(iso, amount, symbol, fromAddress, toAddress, fromNetwork, toNetwork, exchangeGasAmount, toAmount) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceTransaction(address: String, id: String): Either<Failure, List<BinanceTransaction>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceTransaction(address, id, address) }, { it })
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

    suspend fun getBinanceActive(address: String): Either<Failure, List<BinanceStatus>> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceActive(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getBinanceQuota(iso: String, address: String): Either<Failure, BinanceQuota> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getBinanceQuota(iso, address) }, { it })
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

    suspend fun sendFcmToken(token: String, addresses: List<String>): Either<Failure, Any> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.sendFcmToken(SendFcmTokenRequest(token, addresses)) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getIntercomHash(id: String, iso: String): Either<Failure, GetIntercomHashResponse> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getIntercomHash(id, iso) }, { it })
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
