package com.myetherwallet.mewwalletbl.util

import android.app.Activity
import com.myetherwallet.mewwalletbl.connection.ServiceAlarmReceiver

/**
 * Created by BArtWell on 23.07.2019.
 */

object ActivityLifecycleHelper {

    fun onResume(activity: Activity) {
        ServiceAlarmReceiver.cancel(activity)
    }

    fun onPause(activity: Activity) {
        ServiceAlarmReceiver.schedule(activity)
    }
}