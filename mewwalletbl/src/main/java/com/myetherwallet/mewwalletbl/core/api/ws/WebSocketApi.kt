package com.myetherwallet.mewwalletbl.core.api.ws

import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.data.Method
import com.myetherwallet.mewwalletbl.data.api.SendFcmTokenRequest
import com.myetherwallet.mewwalletbl.data.ws.GetIntercomHashRequest
import com.myetherwallet.mewwalletbl.data.ws.GetIntercomHashResponse
import java.util.concurrent.CountDownLatch

/**
 * Created by BArtWell on 13.09.2021.
 */

class WebSocketApi {

    fun sendFcmToken(request: SendFcmTokenRequest): Any {
        return request(Method.PUT, "push/register/android", request, Any::class.java)
    }

    fun getIntercomHash(request: GetIntercomHashRequest): Either<Failure.WebSocketError, GetIntercomHashResponse> {
        return request(Method.GET, "/v2/support/verification", request, GetIntercomHashResponse::class.java)
    }

    private fun <REQUEST, RESPONSE : Any> request(method: Method, path: String, data: REQUEST, responseClass: Class<RESPONSE>): Either<Failure.WebSocketError, RESPONSE> {
        return WebSocketClient.client?.let {
            val countDownLatch = CountDownLatch(1)
            var result: Either<Failure.WebSocketError, RESPONSE>? = null
            WebSocketClient.client?.send(method, path, data, responseClass) {
                result = it
                countDownLatch.countDown()
            }
            countDownLatch.await()
            result!!
        } ?: Either.Left(Failure.WEB_SOCKET_NOT_CONNECTED)
    }
}
