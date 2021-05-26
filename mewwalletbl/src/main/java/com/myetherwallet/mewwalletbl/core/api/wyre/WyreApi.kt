package com.myetherwallet.mewwalletbl.core.api.wyre

import com.myetherwallet.mewwalletbl.data.wyre.WyreRequestParams
import com.myetherwallet.mewwalletbl.data.wyre.WyreReservationResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface WyreApi {
    @POST("v3/orders/reserve")
    @Headers("Authorization: Bearer ", "Content-type: application/json")
    fun getReservation(@Body requestParams: WyreRequestParams): Call<WyreReservationResult>
}