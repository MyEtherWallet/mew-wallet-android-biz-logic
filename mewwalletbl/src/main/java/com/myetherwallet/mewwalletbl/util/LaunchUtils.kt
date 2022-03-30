package com.myetherwallet.mewwalletbl.util

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.myetherwallet.mewwalletbl.R


/**
 * Created by BArtWell on 23.07.2019.
 */

private const val URI_PREFIX_PHONE = "tel:"
private const val URI_MAIL_FORMAT = "mailto:%1\$s?subject=%2\$s&body=%3\$s"
const val PLAY_MARKET_URL = "https://play.google.com/store/apps/details?id="

object LaunchUtils {

    fun openMailApp(context: Context?, email: String?, subject: String? = null, text: String? = null, attachments: List<Uri> = emptyList()) {
        if (context != null && email != null) {
            try {
                val intent = prepareMailToIntent(arrayOf(email), subject ?: "", text ?: "", attachments)
                context.startActivity(Intent.createChooser(intent, context.getString(R.string.feedback_chooser_title)))
            } catch (e: ActivityNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    private fun prepareMailToIntent(addresses: Array<String>, subject: String, body: String, attachments: List<Uri>): Intent {
        val mailToScheme = String.format(URI_MAIL_FORMAT, TextUtils.join(",", addresses), subject, body)
        val uri = Uri.parse(mailToScheme.replace(" ", "%20"))
        val hasAttachments = attachments.isNotEmpty()
        val mailToIntent = Intent(if (hasAttachments) Intent.ACTION_SEND_MULTIPLE else Intent.ACTION_SENDTO, uri)
        mailToIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        mailToIntent.putExtra(Intent.EXTRA_EMAIL, addresses)
        mailToIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mailToIntent.putExtra(Intent.EXTRA_TEXT, body)
        if (hasAttachments) {
            mailToIntent.type = "text/plain"
            mailToIntent.putExtra(Intent.EXTRA_STREAM, ArrayList(attachments))
        }
        return mailToIntent
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
            openWebSite(context, PLAY_MARKET_URL + context.packageName)
        }
    }

    fun shareText(context: Context?, title: String?, text: String?) {
        context?.let  {
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, text)
                type = "text/plain"
            }
            val chooser = Intent.createChooser(intent, title)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val excludedComponents = arrayOf(
                    ComponentName("com.facebook.katana", "com.facebook.composer.shareintent.ImplicitShareIntentHandlerDefaultAlias"),
                    ComponentName("com.facebook.katana", "com.facebook.composer.shareintent.ShareToGroupsAlias")
                )
                chooser.putExtra(Intent.EXTRA_EXCLUDE_COMPONENTS, excludedComponents)
            }
            context.startActivity(chooser)
        }
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        context.startActivity(intent)
    }
}
