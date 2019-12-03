package com.myetherwallet.mewwalletbl.connection

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import com.myetherwallet.mewwalletbl.core.MewLog
import java.util.*

/**
 * Created by BArtWell on 23.07.2019.
 */

private const val TAG = "ServiceAlarmReceiver"
private const val REQUEST_CODE = 1

class ServiceAlarmReceiver : BroadcastReceiver() {

    companion object {

        fun schedule(context: Context) {
            val time = calculateNextStartTime()
            val intent = Intent(context, ServiceAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
            MewLog.d(TAG, "Scheduled on " + DateFormat.format("dd/MM/yyyy HH:mm:ss", time))
        }

        fun cancel(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ServiceAlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.cancel(pendingIntent)
        }

        private fun calculateNextStartTime(): Long {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.MINUTE, 5)
            return calendar.timeInMillis
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        MewLog.d(TAG, "Receive at " + DateFormat.format("dd/MM/yyyy HH:mm:ss", System.currentTimeMillis()))
        MewConnectService.stop(context)
    }
}
