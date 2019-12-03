package com.myetherwallet.mewwalletbl.connection.webrtc

import com.myetherwallet.mewwalletbl.core.MewLog
import org.webrtc.DataChannel

/**
 * Created by BArtWell on 24.07.2019.
 */

private const val TAG = "DataChannelObserver"

open class DataChannelObserver(
    private val onStateChangeListener: () -> Unit,
    private val onMessageListener: (buffer: DataChannel.Buffer) -> Unit
) : DataChannel.Observer {

    override fun onMessage(buffer: DataChannel.Buffer) {
        MewLog.d(TAG, "onMessage")
        onMessageListener.invoke(buffer)
    }

    override fun onBufferedAmountChange(amout: Long) {
        MewLog.d(TAG, "onBufferedAmountChange")
    }

    override fun onStateChange() {
        MewLog.d(TAG, "onStateChange")
        onStateChangeListener.invoke()
    }
}
