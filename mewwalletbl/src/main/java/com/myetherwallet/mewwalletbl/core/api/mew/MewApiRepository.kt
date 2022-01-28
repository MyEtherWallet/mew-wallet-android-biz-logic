package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.BaseRepository
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.MarketPriceVolumeHistory
import com.myetherwallet.mewwalletbl.data.PriceHistoryItem
import com.myetherwallet.mewwalletbl.data.VolumeHistoryItem
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.data.api.lido.LidoInfo
import com.myetherwallet.mewwalletbl.data.api.lido.LidoTransactionResult
import com.myetherwallet.mewwalletbl.data.staked.StakedSubmitTransactionRequest
import com.myetherwallet.mewwalletbl.util.NetworkHandler
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import com.myetherwallet.mewwalletkit.core.extension.*
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

class MewApiRepository(private val service: MewApi) : BaseRepository() {

    suspend fun getBalances(blockchain: Blockchain, address: Address) = requestSuspend({ service.getBalances(blockchain.symbol, address.address) }, { it })

    suspend fun getTransactionHistory(address: String, request: RequestByContracts) = requestSuspend({ service.getTransactionHistory(address, request) }, { it })

    suspend fun getTransaction(hash: String) = requestSuspend({ service.getTransaction(hash) }, { it })

    suspend fun getSurveyUrl(iso: String) = requestSuspend({ service.getSurveyUrl(iso) }, { it.url })

    suspend fun getPurchaseHistory(id: String, paginationToken: String?) = requestSuspend({ service.getPurchaseHistory(hashId(id), paginationToken) }, { it })

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

    suspend fun getPurchaseProvider(cryptoCurrency: String, iso: String) = requestSuspend({ service.getPurchaseProviders(cryptoCurrency, iso) }, { it })

    suspend fun getMarketOverview(period: String?) = requestSuspend({ service.getMarketOverview(period) }, { it })

    suspend fun getMarketMovers() = requestSuspend({ service.getMarketMovers() }, { it })

    suspend fun getMarketCollections() = requestSuspend({ service.getMarketCollections() }, { it })

    suspend fun getMarketPrices(blockchain: Blockchain, paginationToken: Int) = requestSuspend({ service.getMarketPrices(blockchain.symbol, paginationToken) }, { it })

    suspend fun searchMarketPrices(blockchain: Blockchain, query: String) = requestSuspend({ service.searchMarketPrices(blockchain.symbol, query) }, { it })

    suspend fun getMarketInfo(blockchain: Blockchain, address: String) = requestSuspend({ service.getMarketInfo(blockchain.symbol, address) }, { it })

    suspend fun getPriceHistory(blockchain: Blockchain, address: String, from: Date, to: Date): Either<Failure, MarketPriceVolumeHistory> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        return requestSuspend({ service.getPriceHistory(blockchain.symbol, address, from.time / 1000, to.time / 1000) }) {
            MarketPriceVolumeHistory(
                it.prices.map { item ->
                    PriceHistoryItem(dateFormat.parse(item[0])!!, item[1].toBigDecimal())
                },
                it.volumes.map { item ->
                    VolumeHistoryItem(dateFormat.parse(item[0])!!, item[1].toBigDecimal())
                }
            )
        }
    }

    suspend fun getSwapList(blockchain: Blockchain) = requestSuspend({ service.getSwapList(blockchain.symbol) }, { it })

    suspend fun getSwapPrice(blockchain: Blockchain, fromContract: String, toContract: String, amount: String, includeFees: Boolean) =
        requestSuspend({ service.getSwapPrice(blockchain.symbol, fromContract, toContract, amount, includeFees) }, { it })

    suspend fun getSwapTrade(blockchain: Blockchain, address: String, dex: String, exchange: String, fromContract: String, toContract: String, amount: String) =
        requestSuspend({ service.getSwapTrade(blockchain.symbol, address, dex, exchange, fromContract, toContract, amount, "android") }, { it })

    suspend fun getBinanceList(iso: String) = requestSuspend({ service.getBinanceList(iso) }, { it })

    suspend fun getBinanceNetworks(iso: String, symbol: String) = requestSuspend({ service.getBinanceNetworks(iso, symbol) }, { it })

    suspend fun getYearnDeposit(address: Address, amount: String, token: String) = requestSuspend({ service.getYearnDeposit(address, amount, token) }, { it })

    suspend fun getYearnWithdraw(address: Address, amount: String, token: String) = requestSuspend({ service.getYearnWithdraw(address, amount, token) }, { it })

    suspend fun getYearnBalance(address: Address) = requestSuspend({ service.getYearnBalance(address) }, { it })

    suspend fun getYearnInfo() = requestSuspend({ service.getYearnInfo() }, { it })

    suspend fun getLidoInfo(): Either<Failure, LidoInfo> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getLidoInfo() }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun getLidoBalance(address: Address): Either<Failure, TokenBalance> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.getLidoBalance(address) }, { it })
            false -> Either.Left(Failure.NetworkConnection())
        }
    }

    suspend fun submitLido(address: Address, amount: String): Either<Failure, LidoTransactionResult> {
        return when (NetworkHandler.isNetworkConnected()) {
            true -> requestSuspend({ service.submitLido(address, amount) }, { it })
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
    ) = requestSuspend({ service.createBinanceSwap(iso, amount, symbol, fromAddress, toAddress, fromNetwork, toNetwork, exchangeGasAmount, toAmount) }, { it })

    suspend fun getBinanceTransaction(address: String, id: String) = requestSuspend({ service.getBinanceTransaction(address, id, address) }, { it })

    suspend fun getBinanceStatus(address: String, id: String) = requestSuspend({ service.getBinanceStatus(address, id) }, { it })

    suspend fun getBinanceHistory(address: String) = requestSuspend({ service.getBinanceHistory(address) }, { it })

    suspend fun getBinanceActive(address: String) = requestSuspend({ service.getBinanceActive(address) }, { it })

    suspend fun getStakedInfo() = requestSuspend({ service.getStakedInfo() }, { it })

    suspend fun getStakedTransactions(eth2Address: String, amount: Int) = requestSuspend({ service.getStakedTransactions(eth2Address, amount) }, { it })

    suspend fun getStakedHistory(address: String) = requestSuspend({ service.getStakedHistory(address) }, { it })

    suspend fun getStakedStatus(address: String, uuid: String) = requestSuspend({ service.getStakedStatus(address, uuid) }, { it })

    suspend fun getStakedTransaction(address: String, uuid: String) = requestSuspend({ service.getStakedTransaction(address, uuid) }, { it })

    suspend fun getStakedProvision(address: String, eth2Address: String, amount: Int) = requestSuspend({ service.getStakedProvision(address, eth2Address, amount) }, { it })

    suspend fun submitTransaction(request: StakedSubmitTransactionRequest) = requestSuspend({ service.submitTransaction(request) }, { it })

    suspend fun getEstimateTransactionSpeed(request: RequestEstimateByPrice) = requestSuspend({ service.getEstimateTransactionSpeed(request) }, { it })

    suspend fun sendFcmToken(token: String, addresses: List<String>) = requestSuspend({ service.sendFcmToken(SendFcmTokenRequest(token, addresses)) }, { it })

    suspend fun getIntercomHash(id: String, iso: String) = requestSuspend({ service.getIntercomHash(id, iso) }, { it })

    suspend fun getTransactionType(address: Address) = requestSuspend({ service.getAddressTypeInfo(address) }, { it })

    suspend fun getExchangeRates() = requestSuspend({ service.getExchangeRates() }, { it })

    suspend fun getMarketTokens(contracts: MarketTokensRequest) = requestSuspend({ service.getMarketTokens(contracts) }, { it })
}
