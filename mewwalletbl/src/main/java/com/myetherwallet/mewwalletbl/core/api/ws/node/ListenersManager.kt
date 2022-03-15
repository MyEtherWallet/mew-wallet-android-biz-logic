package com.myetherwallet.mewwalletbl.core.api.ws.node

import com.google.gson.JsonElement
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.ws.node.data.Subscription
import com.myetherwallet.mewwalletbl.core.json.JsonParser

/**
 * Created by BArtWell on 17.02.2022.
 */

private const val TAG = "ListenersManager"

class ListenersManager {

    private val listeners = hashMapOf<String, HashMap<Int, ListenerInfo<*>>>()

    fun <T> add(ownerId: Int, subscription: Subscription, callback: (T) -> Unit, clazz: Class<T>) {
        val ownersMap = listeners.getOrPut(subscription.raw) { HashMap() }
        ownersMap[ownerId] = ListenerInfo(callback, clazz)
    }

    fun removeOwner(ownerId: Int, subscription: Subscription) {
        listeners[subscription.raw]?.remove(ownerId)
    }

    fun removeAllOwners(subscription: Subscription) {
        listeners.remove(subscription.raw)
    }

    fun call(subscription: Subscription, json: JsonElement) {
        listeners[subscription.raw]?.let {
            for ((_, listenerInfo) in it) {
                listenerInfo.callListener(json)
            }
        }
    }

    class ListenerInfo<T>(private val callback: (T) -> Unit, private val clazz: Class<T>) {

        fun callListener(json: JsonElement) {
            try {
                val result = JsonParser.fromJson(json, clazz)
                callback(result)
            } catch (e: Exception) {
                MewLog.e(TAG, "Unable to parse event", e)
            }
        }
    }
}
