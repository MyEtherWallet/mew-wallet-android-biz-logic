package com.myetherwallet.mewwalletbl.core.api.ws.node.data

/**
 * Created by BArtWell on 15.02.2022.
 */

enum class Subscription(val raw: String) {
    NEW_HEADS("newHeads"),
    NEW_PENDING_TRANSACTIONS("newPendingTransactions");

    companion object {

        fun find(raw: String) = values().find { it.raw == raw }
    }
}
