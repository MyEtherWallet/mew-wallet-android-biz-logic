package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.data.PurchaseHistory
import com.myetherwallet.mewwalletbl.data.PurchaseProviderResponse
import com.myetherwallet.mewwalletbl.data.PurchaseSimplexOrder
import com.myetherwallet.mewwalletbl.data.PurchaseSimplexQuote
import com.myetherwallet.mewwalletbl.data.api.*
import retrofit2.Call
import retrofit2.http.*
import java.math.BigDecimal

/**
 * Created by BArtWell on 13.09.2019.
 */

interface MewApi {

    @POST("prices")
    @Headers("content-type: application/json")
    fun getPrices(@Body request: RequestByContracts): Call<List<Price>>

    @GET("tokens/account")
    @Headers("content-type: application/json")
    fun getAccountTokens(@Query("address") address: String): Call<List<Token>>

    @POST("tokens")
    @Headers("content-type: application/json")
    fun getTokens(@Body request: RequestByContracts): Call<List<Token>>

    @GET("tokens/meta")
    @Headers("content-type: application/json")
    fun getMetaTokens(): Call<List<Token>>

    @POST("balances")
    @Headers("content-type: application/json")
    fun getBalances(@Query("address") address: String, @Query("limit") limit: Int, @Body request: RequestByContracts): Call<List<TokenBalance>>

    @GET("balances/account")
    @Headers("content-type: application/json")
    fun getBalances(@Query("address") address: String): Call<List<TokenBalance>>

    @POST("transactions/history")
    @Headers("content-type: application/json")
    fun getTransactionHistory(@Query("address") address: String, @Query("limit") limit: Int, @Query("offset") offset: Int, @Body request: RequestByContracts): Call<List<Transaction>>

    @GET("transactions/{hash}")
    @Headers("content-type: application/json")
    fun getTransaction(@Path("hash") address: String): Call<Transaction>

    @PUT("push/register/android")
    fun sendFcmToken(@Body body: SendFcmTokenRequest): Call<Any>

    @GET("purchase/history")
    @Headers("content-type: application/json")
    fun getPurchaseHistory(@Query("id") id: String, @Query("paginationToken") paginationToken: String?): Call<PurchaseHistory>

    @GET("/purchase/simplex/order")
    @Headers("content-type: application/json")
    fun getPurchaseSimplexOrder(@Query("id") id: String, @Query("paymentId") paymentId: String, @Query("address") address: String): Call<PurchaseSimplexOrder>

    @GET("/purchase/simplex/quote")
    @Headers("content-type: application/json")
    fun getPurchaseSimplexQuote(@Query("id") id: String, @Query("fiatCurrency") fiatCurrency: String, @Query("requestedCurrency") requestedCurrency: String, @Query("requestedAmount") requestedAmount: BigDecimal): Call<PurchaseSimplexQuote>

    @GET("/purchase/providers/android")
    @Headers("content-type: application/json")
    fun getPurchaseProvider(@Query("iso") iso: String): Call<PurchaseProviderResponse>

    @GET("prices/list")
    @Headers("content-type: application/json")
    fun getMarketPrices(@Query("paginationToken") paginationToken: String?): Call<Market>

    @GET("prices/search")
    @Headers("content-type: application/json")
    fun searchMarketPrices(@Query("q") query: String): Call<List<MarketItem>>

    @GET("prices/market")
    @Headers("content-type: application/json")
    fun getMarketInfo(@Query("contractAddress") address: String): Call<MarketItem>
}
