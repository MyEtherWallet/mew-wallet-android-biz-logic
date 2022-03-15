package com.myetherwallet.mewwalletbl.core.api.ws.node

import androidx.lifecycle.ViewModel
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.EventNewHeads
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.EventNewPendingTransactions
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.GasInfo
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.Subscription
import com.myetherwallet.mewwalletbl.data.BlockResponse
import com.myetherwallet.mewwalletbl.data.JsonRpcRequest
import java.math.BigInteger

/**
 * Created by BArtWell on 14.02.2022.
 */

private const val TAG = "NodeWebSocketApi"

class NodeWebSocketApi(private val client: NodeWebSocketClient?) {

    private var lastHandlerGasPriceBlockNumber = BigInteger.ZERO
    private var lastKnownGasInfo: GasInfo? = null

    fun subscribe(subscription: Subscription) = client?.subscribe(subscription)

    fun unsubscribe(subscription: Subscription) = client?.unsubscribe(subscription)

    fun listenNewHeads(owner: ViewModel, callback: (EventNewHeads) -> Unit) {
        client?.addListener(owner.hashCode(), Subscription.NEW_HEADS, callback, EventNewHeads::class.java)
    }

    fun unlistenNewHeads(owner: ViewModel) = client?.removeListener(owner.hashCode(), Subscription.NEW_HEADS)

    fun listenNewPendingTransactions(owner: ViewModel, callback: (EventNewPendingTransactions) -> Unit) {
        client?.addListener(owner.hashCode(), Subscription.NEW_PENDING_TRANSACTIONS, callback, EventNewPendingTransactions::class.java)
    }

    fun unlistenNewPendingTransactions(owner: ViewModel) = client?.removeListener(owner.hashCode(), Subscription.NEW_PENDING_TRANSACTIONS)

    fun listenGasPrice(owner: ViewModel, gasPriceCallback: (GasInfo) -> Unit) {
        if (lastKnownGasInfo == null) {
            MewLog.d(TAG, "No cached gas info, request block")
            getPendingBlock { block ->
                getGasPrice { gasPrice ->
                    MewLog.d(TAG, "Return gas info with block request")
                    lastKnownGasInfo = GasInfo(block, gasPrice)
                    gasPriceCallback(lastKnownGasInfo!!)
                }
            }
        } else {
            MewLog.d(TAG, "Return cached gas info")
            gasPriceCallback(lastKnownGasInfo!!)
        }
        listenNewHeads(owner) { newHeads ->
            if (newHeads.number - lastHandlerGasPriceBlockNumber > BigInteger.valueOf(100)) {
                lastHandlerGasPriceBlockNumber = newHeads.number
                getGasPrice { gasPrice ->
                    MewLog.d(TAG, "Return gas info with newHeads event")
                    lastKnownGasInfo = GasInfo(newHeads, gasPrice)
                    gasPriceCallback(lastKnownGasInfo!!)
                }
            } else {
                lastKnownGasInfo = lastKnownGasInfo?.let { lastInfo ->
                    GasInfo(newHeads.baseFeePerGas, lastInfo.tip).also {
                        MewLog.d(TAG, "Return cached gas info")
                        gasPriceCallback(it)
                    }
                }
            }
        }
    }

    private fun getPendingBlock(callback: (BlockResponse) -> Unit) {
        val request = JsonRpcRequest<Any>(JsonRpcRequest.Method.GET_BLOCK_BY_NUMBER.methodName, listOf("pending", false))
        client?.requestRpc(request, BlockResponse::class.java) { either ->
            either.getRightOrNull()?.let {
                callback(it)
            }
        }
    }

    private fun getGasPrice(callback: (BigInteger) -> Unit) {
        val request = JsonRpcRequest<Unit>(JsonRpcRequest.Method.GAS_PRICE.methodName, emptyList())
        client?.requestRpc(request, BigInteger::class.java) { either ->
            either.getRightOrNull()?.let {
                callback(it)
            }
        }
    }

    fun unlistenGasPrice(owner: ViewModel) = unlistenNewHeads(owner)
}
