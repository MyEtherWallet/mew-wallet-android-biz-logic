package com.myetherwallet.mewwalletbl.core.api.ws

import com.myetherwallet.mewwalletbl.core.api.ws.mew.MewWebSocketApi
import com.myetherwallet.mewwalletbl.core.api.ws.mew.MewWebSocketClient
import com.myetherwallet.mewwalletbl.core.api.ws.node.NodeWebSocketApi
import com.myetherwallet.mewwalletbl.core.api.ws.node.NodeWebSocketClient

/**
 * Created by BArtWell on 15.02.2022.
 */

object WebSocketManager {

    private var mewClient: MewWebSocketClient? = null
    var mewApi: MewWebSocketApi? = null
    private var nodeClient: NodeWebSocketClient? = null
    var nodeApi: NodeWebSocketApi? = null

    fun connect() {
//        if (mewClient == null) {
//            mewClient = MewWebSocketClient()
//            mewClient?.connect()
//            mewApi = MewWebSocketApi(mewClient)
//        }
        if (nodeClient == null) {
            nodeClient = NodeWebSocketClient()
            nodeClient?.connect()
            nodeApi = NodeWebSocketApi(nodeClient)
        }
    }

    fun disconnect() {
        mewClient?.disconnect()
        mewApi = null
        mewClient = null
        nodeClient?.disconnect()
        nodeApi = null
        nodeClient = null
    }
}
