package com.myetherwallet.mewwalletbl.core.api.ws.node

import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.myetherwallet.mewwalletbl.BuildConfig
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Either
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.api.ws.BaseWebSocketClient
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.NodeEvent
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.SubscribeRequest
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.Subscription
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.UnsubscribeResponse
import com.myetherwallet.mewwalletbl.core.json.JsonParser
import com.myetherwallet.mewwalletbl.data.JsonRpcRequest

/**
 * Created by BArtWell on 14.02.2022.
 */

private const val TAG = "NodeWebSocketClient"
private const val SOCKET_URL = BuildConfig.NODE_SOCKET_END_POINT

class NodeWebSocketClient : BaseWebSocketClient(SOCKET_URL, NodeParser()) {

    private var subscriptionIds = hashMapOf<String, Subscription>()
    private val listenersManager = ListenersManager()
    private val lastReceivedEvents = hashMapOf<Subscription, JsonElement>()

    fun subscribe(subscription: Subscription, callback: ((error: String?) -> Unit)? = null) {
        if (subscriptionIds.containsValue(subscription)) {
            MewLog.w(TAG, "Already subscribed to " + subscription.raw + ", ignored")
            return
        }
        val id = getNextId()
        sendForResponse(id, SubscribeRequest(id, SubscribeRequest.Method.SUBSCRIBE, subscription), String::class.java) {
            if (it.isRight) {
                val subscriptionId = it.getRightOrNull()!!
                subscriptionIds[subscriptionId] = subscription
                callback?.invoke(null)
            } else {
                MewLog.e(TAG, "Subscribe error", it.getLeftOrNull()?.throwable)
                callback?.invoke(it.getLeftOrNull()?.error!!)
            }
        }
    }

    fun unsubscribe(subscription: Subscription, callback: ((error: String?) -> Unit)? = null) {
        listenersManager.removeAllOwners(subscription)
        subscriptionIds = HashMap(subscriptionIds.filterValues { it != subscription })
        val id = getNextId()
        sendForResponse(id, SubscribeRequest(id, SubscribeRequest.Method.UNSUBSCRIBE, subscription), UnsubscribeResponse::class.java) {
            if (it.isRight) {
                val unsubscribeResult = it.getRightOrNull()!!.result
                if (unsubscribeResult) {
                    callback?.invoke(null)
                } else {
                    callback?.invoke("Unknown error while event unsubscribing")
                }
            } else {
                MewLog.e(TAG, "Unsubscribe error", it.getLeftOrNull()?.throwable)
                callback?.invoke(it.getLeftOrNull()?.error!!)
            }
        }
    }

    fun <T> addListener(ownerId: Int, subscription: Subscription, callback: (T) -> Unit, clazz: Class<T>) {
        MewLog.i(TAG, "Listen $subscription for $ownerId")
        lastReceivedEvents[subscription]?.let {
            listenersManager.call(subscription, it)
        }
        listenersManager.add(ownerId, subscription, callback, clazz)
    }

    fun removeListener(ownerId: Int, subscription: Subscription) {
        MewLog.i(TAG, "Unlisten $subscription for $ownerId")
        listenersManager.removeOwner(ownerId, subscription)
    }

    override fun handleEvent(data: String) {
        val event = parseEvent(data)
        if (event.method == NodeEvent.SUBSCRIPTION_METHOD) {
            subscriptionIds[event.params.subscription]?.let { entry ->
                lastReceivedEvents[entry] = event.params.result
                listenersManager.call(entry, event.params.result)
            }
        }
    }

    private fun parseEvent(data: String): NodeEvent<JsonElement> {
        val type = TypeToken.getParameterized(NodeEvent::class.java, JsonElement::class.java).type
        return JsonParser.fromJson(data, type)
    }

    fun <IN, OUT : Any> requestRpc(request: JsonRpcRequest<IN>, clazz: Class<OUT>, callback: (Either<Failure.WebSocketError, OUT>) -> Unit) {
        val id = getNextId()
        request.id = id
        sendForResponse(id, request, clazz, callback)
    }
}
