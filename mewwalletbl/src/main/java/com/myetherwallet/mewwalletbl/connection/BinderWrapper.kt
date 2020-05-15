package com.myetherwallet.mewwalletbl.connection

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.myetherwallet.mewwalletbl.core.MewLog

/**
 * Created by BArtWell on 08.11.2019.
 */

private const val TAG = "BinderWrapper"

class BinderWrapper<T : Service>(private val context: Context, private val intent: Intent) {

    var isBinded = false
        private set
    var service: T? = null
        private set
    private lateinit var serviceConnection: ServiceConnection

    fun bind(onConnected: () -> Unit, onDisconnected: (() -> Unit)? = null) {
        MewLog.d(TAG, "Bind service")
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, binder: IBinder) {
                MewLog.d(TAG, "Service bound")
                service = (binder as ServiceBinder<T>).service
                isBinded = true
                onConnected()

            }

            override fun onServiceDisconnected(name: ComponentName) {
                MewLog.d(TAG, "Service unbound")
                isBinded = false
                onDisconnected?.invoke()
                service = null
            }
        }
        context.bindService(intent, serviceConnection, 0)
    }

    fun unbind() {
        if (isBinded) {
            try {
                context.unbindService(serviceConnection)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}