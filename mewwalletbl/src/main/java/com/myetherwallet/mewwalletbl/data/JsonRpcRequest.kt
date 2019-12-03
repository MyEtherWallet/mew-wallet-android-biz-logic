package com.myetherwallet.mewwalletbl.data

import com.google.gson.annotations.SerializedName
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by BArtWell on 13.09.2019.
 */

class JsonRpcRequest<T>(
    @SerializedName("method")
    private val method: String,
    @SerializedName("params")
    private val params: List<T>) {

    companion object {
        private val nextId = AtomicInteger(0)
    }

    @SerializedName("jsonrpc")
    val version: String = "2.0"
    @SerializedName("id")
    val id = nextId.getAndIncrement()

    enum class Method(val methodName: String) {
        GET_BALANCE("eth_getBalance"),
        GAS_PRICE("eth_gasPrice"),
        GET_TRANSACTION_COUNT("eth_getTransactionCount"),
        SEND_RAW_TRANSACTION("eth_sendRawTransaction");

        override fun toString() = methodName
    }
}
