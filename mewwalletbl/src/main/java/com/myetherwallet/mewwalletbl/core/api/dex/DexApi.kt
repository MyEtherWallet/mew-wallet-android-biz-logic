package com.myetherwallet.mewwalletbl.core.api.dex

import com.myetherwallet.mewwalletbl.data.dex.DexPrice
import com.myetherwallet.mewwalletbl.data.dex.DexToken
import com.myetherwallet.mewwalletbl.data.dex.DexTrade
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface DexApi {

    @GET("token-list")
    @Headers("content-type: application/json")
    fun getTokenList(): Call<List<DexToken>>

    @GET("token-list-full")
    @Headers("content-type: application/json")
    fun getFullTokenList(): Call<List<DexToken>>

    @GET("price")
    @Headers("content-type: application/json")
    fun getPriceFrom(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("fromAmount") fromAmount: String,
        @Query("dex") dex: String
    ): Call<DexPrice>

    @GET("price")
    @Headers("content-type: application/json")
    fun getPriceTo(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("toAmount") toAmount: String,
        @Query("dex") dex: String
    ): Call<DexPrice>

    @GET("price?dex=all")
    @Headers("content-type: application/json")
    fun getPricesFrom(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("fromAmount") fromAmount: String
    ): Call<List<DexPrice>>

    @GET("price?dex=all")
    @Headers("content-type: application/json")
    fun getPricesTo(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("toAmount") toAmount: String
    ): Call<List<DexPrice>>

    @GET("trade")
    @Headers("content-type: application/json")
    fun getTradeFrom(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("fromAmount") fromAmount: String,
        @Query("dex") dex: String,
        @Query("proxy") proxy: String
    ): Call<DexTrade>

    @GET("trade")
    @Headers("content-type: application/json")
    fun getTradeTo(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("toAmount") toAmount: String,
        @Query("dex") dex: String,
        @Query("proxy") proxy: String
    ): Call<DexTrade>
}