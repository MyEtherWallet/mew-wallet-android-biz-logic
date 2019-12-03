package com.myetherwallet.mewwalletbl.core.api.mew

import com.myetherwallet.mewwalletbl.data.api.*
import retrofit2.Call
import retrofit2.http.*

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
    fun getTokens(@Body body: List<String>?): Call<List<Token>>

    @GET("tokens/meta")
    @Headers("content-type: application/json")
    fun getMetaTokens(): Call<List<Token>>

    @POST("balances")
    @Headers("content-type: application/json")
    fun getBalances(@Query("address") address: String, @Query("limit") limit: Int, @Body request: RequestByContracts): Call<List<TokenBalance>>

    @POST("transactions/history")
    @Headers("content-type: application/json")
    fun getTransactionHistory(@Query("address") address: String, @Query("limit") limit: Int, @Query("offset") offset: Int, @Body request: RequestByContracts): Call<List<Transaction>>

    @GET("transactions/{hash}")
    @Headers("content-type: application/json")
    fun getTransaction(@Path("hash") address: String): Call<Transaction>

    @PUT("push/register/android")
    fun sendFcmToken(@Body body: SendFcmTokenRequest): Call<Any>
}
