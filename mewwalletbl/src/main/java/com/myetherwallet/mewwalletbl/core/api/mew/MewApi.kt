package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.data.*
import com.myetherwallet.mewwalletbl.data.api.*
import com.myetherwallet.mewwalletbl.data.api.binance.BinanceNetwork
import com.myetherwallet.mewwalletbl.data.api.binance.BinanceStatus
import com.myetherwallet.mewwalletbl.data.api.binance.BinanceToken
import com.myetherwallet.mewwalletbl.data.api.binance.BinanceTransaction
import com.myetherwallet.mewwalletbl.data.api.lido.LidoInfo
import com.myetherwallet.mewwalletbl.data.api.lido.LidoTransactionResult
import com.myetherwallet.mewwalletbl.data.api.market.MarketCollectionItem
import com.myetherwallet.mewwalletbl.data.api.market.MarketItem
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnBalance
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnDepositResult
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnInfo
import com.myetherwallet.mewwalletbl.data.dex.DexPriceResult
import com.myetherwallet.mewwalletbl.data.dex.DexTradeResult
import com.myetherwallet.mewwalletbl.data.staked.*
import com.myetherwallet.mewwalletbl.data.ws.GetIntercomHashResponse
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import retrofit2.http.*

/**
 * Created by BArtWell on 13.09.2019.
 */

interface MewApi {

    @GET("v3/balances/account")
    @Headers("content-type: application/json")
    suspend fun getBalances(@Query("chain") blockchain: String, @Query("address") address: String): List<TokenBalance>

    @POST("/v2/transactions/history")
    @Headers("content-type: application/json")
    suspend fun getTransactionHistory(@Query("address") address: String, @Body request: RequestByContracts): List<Transaction>

    @GET("transactions/{hash}")
    @Headers("content-type: application/json")
    suspend fun getTransaction(@Path("hash") address: String): Transaction

    @GET("purchase/history")
    @Headers("content-type: application/json")
    suspend fun getPurchaseHistory(@Query("id") id: String, @Query("paginationToken") paginationToken: String?): PurchaseHistory

    @GET("v3/purchase/providers/android")
    @Headers("content-type: application/json")
    suspend fun getPurchaseProviders(@Query(value = "cryptoCurrency") cryptoCurrency: String, @Query("iso") iso: String): List<PurchaseProvider>

    @GET("prices/market")
    @Headers("content-type: application/json")
    suspend fun getMarketInfo(@Query(value = "chain") blockchain: String, @Query("contractAddress") address: String): MarketItem

    @GET("v2/prices/history")
    @Headers("content-type: application/json")
    suspend fun getPriceHistory(@Query(value = "chain") blockchain: String, @Query("contractAddress") address: String, @Query("from") from: Long, @Query("to") to: Long): PriceHistoryResponse

    @GET("v2/prices/list")
    @Headers("content-type: application/json")
    suspend fun getMarketPrices(@Query("chain") blockchain: String, @Query("paginationToken") paginationToken: Int): MarketResponse

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
    suspend fun searchMarketPrices(@Query("chain") blockchain: String, @Query("q") query: String): List<MarketItem>

    @GET("v3/swap/list")
    @Headers("content-type: application/json")
    suspend fun getSwapList(@Query("chain") blockchain: String): List<MarketItem>

    @GET("v3/swap/quote")
    @Headers("content-type: application/json")
    suspend fun getSwapPrice(
        @Query("chain") blockchain: String,
        @Query("fromContractAddress") from: String,
        @Query("toContractAddress") to: String,
        @Query("amount") amount: String,
        @Query("includeFees") includeFees: Boolean
    ): DexPriceResult

    @GET("v3/swap/trade")
    @Headers("content-type: application/json")
    suspend fun getSwapTrade(
        @Query("chain") blockchain: String,
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
    suspend fun getBinanceList(@Query("iso") iso: String): List<BinanceToken>

    @GET("v2/swap/binance/networks")
    @Headers("content-type: application/json")
    suspend fun getBinanceNetworks(
        @Query("iso") iso: String,
        @Query("symbol") symbol: String
    ): List<BinanceNetwork>

    @GET("v2/swap/binance/swap") //?walletNetwork=BSC
    @Headers("content-type: application/json")
    suspend fun createBinanceSwap(
        @Query("iso") iso: String,
        @Query("amount") amount: String,
        @Query("symbol") symbol: String,
        @Query("fromAddress") walletAddress: String?,
        @Query("toAddress") toAddress: String,
        @Query("fromNetwork") fromNetwork: String,
        @Query("toNetwork") toNetwork: String,
        @Query("exchangeGasAmount") gasAmount: String?,
        @Query("toAmount") toAmount: String?
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

    @GET("v2/swap/binance/active")
    @Headers("content-type: application/json")
    suspend fun getBinanceActive(@Query("address") address: String): List<BinanceStatus>

    @GET("/v2/prices/exchange-rates")
    @Headers("content-type: application/json")
    suspend fun getExchangeRates(): List<ExchangeRates>

    @GET("v2/swap/binance/transaction")
    @Headers("content-type: application/json")
    suspend fun getBinanceTransaction(
        @Query("address") address: String,
        @Query("id") id: String,
        @Query("fromAddress") fromAddress: String
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

    @GET("/poll")
    suspend fun getSurveyUrl(@Query("locale") locale: String): SurveyResponse

    @PUT("push/register/android")
    @Headers("content-type: application/json")
    suspend fun sendFcmToken(@Body body: SendFcmTokenRequest): Any

    @GET("/v2/support/verification?platform=android")
    @Headers("content-type: application/json")
    suspend fun getIntercomHash(@Query("id") id: String, @Query("iso") iso: String): GetIntercomHashResponse

    @GET("/v2/yearn/deposit")
    @Headers("content-type: application/json")
    suspend fun getYearnDeposit(@Query("address") address: Address, @Query("amount") amount: String, @Query("token") token: String): YearnDepositResult

    @GET("/v2/yearn/withdraw")
    @Headers("content-type: application/json")
    suspend fun getYearnWithdraw(@Query("address") address: Address, @Query("amount") amount: String, @Query("token") token: String): YearnDepositResult

    @GET("/v2/yearn/balances")
    @Headers("content-type: application/json")
    suspend fun getYearnBalance(@Query("address") address: Address): List<YearnBalance>

    @GET("/v2/yearn/info")
    @Headers("content-type: application/json")
    suspend fun getYearnInfo(): List<YearnInfo>

    @GET("/v2/lido/info")
    @Headers("content-type: application/json")
    suspend fun getLidoInfo(): LidoInfo

    @GET("/v2/lido/submit?platform=android")
    @Headers("content-type: application/json")
    suspend fun submitLido(@Query("address") address: Address, @Query("amount") amount: String): LidoTransactionResult

    @GET("/v2/lido/balance")
    @Headers("content-type: application/json")
    suspend fun getLidoBalance(@Query("address") address: Address): TokenBalance

    @POST("/prices/market")
    @Headers("content-type: application/json")
    suspend fun getMarketTokens(@Body contracts: MarketTokensRequest): List<MarketItem>
}
