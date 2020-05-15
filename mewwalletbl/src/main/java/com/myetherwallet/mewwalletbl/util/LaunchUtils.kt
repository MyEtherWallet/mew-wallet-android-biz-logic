package com.myetherwallet.mewwalletbl.util

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils


/**
 * Created by BArtWell on 23.07.2019.
 */

private const val URI_PREFIX_PHONE = "tel:"

object LaunchUtils {

    fun openMailApp(context: Context?, email: String?, subject: String? = null, text: String? = null) {
        if (context != null && email != null) {
            try {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "message/rfc822"
                if (!TextUtils.isEmpty(subject)) {
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                }
                if (!TextUtils.isEmpty(text)) {
                    intent.putExtra(Intent.EXTRA_TEXT, text)
                }
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                context.startActivity(Intent.createChooser(intent, null))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun openCaller(context: Context?, phone: String?) {
        if (context != null && phone != null) {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse(URI_PREFIX_PHONE + phone)
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun openWebSite(context: Context?, url: String?) {
        if (context != null && url != null) {
            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    fun openMarket(context: Context?, packageName: String? = context?.packageName) {
        context?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            val apps = context.packageManager.queryIntentActivities(intent, 0)
            for (app in apps) {
                if (app.activityInfo.applicationInfo.packageName == "com.android.vending") {
                    val activityInfo = app.activityInfo
                    val componentName = ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.component = componentName
                    context.startActivity(intent)
                    return
                }
            }
            openWebSite(context, "https://play.google.com/store/apps/details?id=" + context.packageName)
        }
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}
