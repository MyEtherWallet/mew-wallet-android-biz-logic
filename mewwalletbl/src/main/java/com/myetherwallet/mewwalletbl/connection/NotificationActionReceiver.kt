package com.myetherwallet.mewwalletbl.connection

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by BArtWell on 23.07.2019.
 */

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        ServiceAlarmReceiver.cancel(context)
        MewConnectService.stop(context)
    }
}
