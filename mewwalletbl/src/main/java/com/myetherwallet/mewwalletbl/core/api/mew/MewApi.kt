package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.data.api.binance.*
import com.myetherwallet.mewwalletbl.data.api.market.MarketCollectionItem
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem
import com.myetherwallet.mewwalletbl.data.dex.DexPriceResult
import com.myetherwallet.mewwalletbl.data.dex.DexToken
import com.myetherwallet.mewwalletbl.data.dex.DexTradeResult
import com.myetherwallet.mewwalletbl.data.staked.*
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import retrofit2.http.*
import java.math.BigDecimal

/**
 * Created by BArtWell on 13.09.2019.
 */

interface MewApi {

    @POST("prices")
    @Headers("content-type: application/json")
    suspend fun getPrices(@Body request: RequestByContracts): List<SimplePrice>

    @POST("tokens")
    @Headers("content-type: application/json")
    suspend fun getTokens(@Body request: RequestByContracts): List<Token>

    @GET("tokens/meta")
    @Headers("content-type: application/json")
    suspend fun getMetaTokens(): List<Token>

    @GET("balances/account")
    @Headers("content-type: application/json")
    suspend fun getBalances(@Query("address") address: String): List<TokenBalance>

    @POST("/v2/transactions/history")
    @Headers("content-type: application/json")
    suspend fun getTransactionHistory(@Query("address") address: String, @Body request: RequestByContracts): List<Transaction>

    @GET("transactions/{hash}")
    @Headers("content-type: application/json")
    suspend fun getTransaction(@Path("hash") address: String): Transaction

    @PUT("push/register/android")
    suspend fun sendFcmToken(@Body body: SendFcmTokenRequest): Any

    @GET("purchase/history")
    @Headers("content-type: application/json")
    suspend fun getPurchaseHistory(@Query("id") id: String, @Query("paginationToken") paginationToken: String?): PurchaseHistory

    @GET("/purchase/simplex/quote")
    @Headers("content-type: application/json")
    suspend fun getPurchaseSimplexQuote(
        @Query("id") id: String,
        @Query("fiatCurrency") fiatCurrency: String,
        @Query("requestedCurrency") requestedCurrency: String,
        @Query("requestedAmount") requestedAmount: BigDecimal
    ): PurchaseSimplexQuote

    @GET("v3/purchase/providers/android")
    @Headers("content-type: application/json")
    suspend fun getPurchaseProviders(@Query("iso") iso: String): List<PurchaseProvider>

    @GET("prices/market")
    @Headers("content-type: application/json")
    suspend fun getMarketInfo(@Query("contractAddress") address: String): MarketItem

    @GET("v2/prices/history")
    @Headers("content-type: application/json")
    suspend fun getPriceHistory(@Query("contractAddress") address: String, @Query("from") from: Long, @Query("to") to: Long): PriceHistoryResponse

    @GET("v2/prices/account")
    @Headers("content-type: application/json")
    suspend fun getPrices(@Query("address") address: String): List<Price>

    @GET("v2/prices/list")
    @Headers("content-type: application/json")
    suspend fun getMarketPrices(@Query("paginationToken") paginationToken: Int): MarketResponse

    @GET("v2/market/collections")
    @Headers("content-type: application/json")
    suspend fun getMarketCollections(): List<MarketCollectionItem>

    @GET("v2/market")
    @Headers("content-type: application/json")
    suspend fun getMarketOverview(@Query("period") period: String?): MarketOverviewResponse

    @GET("v2/market/movers")
    @Headers("content-type: application/json")
    suspend fun getMarketMovers(): List<MarketItem>

    @GET("v2/prices/search")
    @Headers("content-type: application/json")
    suspend fun searchMarketPrices(@Query("q") query: String): List<MarketItem>

    @GET("v2/swap/list")
    @Headers("content-type: application/json")
    suspend fun getSwapList(): List<DexToken>

    @GET("v2/swap/quote")
    @Headers("content-type: application/json")
    suspend fun getSwapPrice(
        @Query("fromContractAddress") from: String,
        @Query("toContractAddress") to: String,
        @Query("amount") amount: String,
        @Query("includeFees") includeFees: Boolean
    ): DexPriceResult

    @GET("v2/swap/trade")
    @Headers("content-type: application/json")
    suspend fun getSwapTrade(
        @Query("address") address: String,
        @Query("dex") dex: String,
        @Query("exchange") exchange: String,
        @Query("fromContractAddress") fromContract: String,
        @Query("toContractAddress") toContract: String,
        @Query("amount") amount: String,
        @Query("platform") platform: String
    ): DexTradeResult

    @GET("v2/swap/binance/list")
    @Headers("content-type: application/json")
    suspend fun getBinanceList(): List<BinanceToken>

    @GET("v2/swap/binance/networks")
    @Headers("content-type: application/json")
    suspend fun getBinanceNetworks(
        @Query("symbol") symbol: String
    ): List<BinanceNetwork>

    @GET("v2/swap/binance/quota")
    @Headers("content-type: application/json")
    suspend fun getBinanceQuota(
        @Query("address") address: String
    ): BinanceQuota

    @GET("v2/swap/binance/swap?walletNetwork=BSC")
    @Headers("content-type: application/json")
    suspend fun createBinanceSwap(
        @Query("amount") amount: String,
        @Query("symbol") symbol: String,
        @Query("fromAddress") walletAddress: String?,
        @Query("toAddress") toAddress: String,
        @Query("fromNetwork") fromNetwork: String,
        @Query("toNetwork") toNetwork: String,
        @Query("toAmount") toAmount: String?,
        @Query("exchangeGasAmount") gasAmount: String?
    ): BinanceStatus

    @GET("v2/swap/binance/status")
    @Headers("content-type: application/json")
    suspend fun getBinanceStatus(
        @Query("address") address: String,
        @Query("id") id: String
    ): BinanceStatus

    @GET("v2/swap/binance/history")
    @Headers("content-type: application/json")
    suspend fun getBinanceHistory(@Query("address") address: String): List<BinanceStatus>

    @GET("/v2/prices/exchange-rates")
    @Headers("content-type: application/json")
    suspend fun getExchangeRates(): List<ExchangeRates>

    @GET("v2/swap/binance/transaction")
    @Headers("content-type: application/json")
    suspend fun getBinanceTransaction(
        @Query("address") address: String,
        @Query("id") id: String
    ): List<BinanceTransaction>

    @GET("/v2/stake/info")
    @Headers("content-type: application/json")
    suspend fun getStakedInfo(): StakedInfo

    @GET("/v2/stake/transaction")
    @Headers("content-type: application/json")
    suspend fun getStakedTransactions(@Query("eth2Address") eth2Address: String, @Query("amount") amount: Int): List<StakedTransactions>

    @GET("/v2/stake/history")
    @Headers("content-type: application/json")
    suspend fun getStakedHistory(@Query("address") address: String): List<StakedHistoryInfo>

    @GET("/v2/stake/status")
    @Headers("content-type: application/json")
    suspend fun getStakedStatus(@Query("address") address: String, @Query("provisioning_request_uuid") uuid: String): StakedHistoryInfo

    @GET("/v2/stake/transaction")
    @Headers("content-type: application/json")
    suspend fun getStakedTransaction(@Query("address") address: String, @Query("provisioning_request_uuid") uuid: String): StakedTransaction

    @GET("/v2/stake/provision")
    suspend fun getStakedProvision(@Query("address") address: String, @Query("withdrawalKey") eth2Address: String, @Query("amount") amount: Int): StakedProvisionResponse

    @POST("/v2/stake/submit")
    @Headers("content-type: application/json")
    suspend fun submitTransaction(@Body request: StakedSubmitTransactionRequest): StakedSubmitTransactionResponse

    @POST("/v2/gas/speed")
    @Headers("content-type: application/json")
    suspend fun getEstimateTransactionSpeed(@Body request: RequestEstimateByPrice): List<EstimateTransactionSpeed>

    @GET("/address/info")
    @Headers("content-type: application/json")
    suspend fun getAddressTypeInfo(@Query("address") address: Address): AddressTypeInfo

    @POST("/v2/survey")
    suspend fun sendSurvey(@Body request: SendSurveyRequest): Any

    @GET("/v2/support/verification?platform=android")
    @Headers("content-type: application/json")
    suspend fun getIntercomHash(@Query("id") id: String, @Query("iso") iso: String): ApiResult<String>
}
