package com.myetherwallet.mewwalletbl.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.os.Build
import android.telephony.TelephonyManager
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.myetherwallet.mewwalletbl.AppActivityImpl
import com.myetherwallet.mewwalletbl.R
import com.myetherwallet.mewwalletbl.core.MewLog
import com.myetherwallet.mewwalletbl.core.api.Failure
import com.myetherwallet.mewwalletbl.core.persist.database.Database
import com.myetherwallet.mewwalletbl.data.AppCurrency
import com.myetherwallet.mewwalletbl.data.AppLanguage
import com.myetherwallet.mewwalletbl.data.KeysStorageType
import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import org.json.JSONObject
import retrofit2.HttpException
import java.math.BigDecimal
import java.util.*


/**
 * Created by BArtWell on 24.02.2020.
 */

private const val TAG = "ApplicationUtils"

object ApplicationUtils {

    const val ETHERIUM_ICON_URL = "https://raw.githubusercontent.com/MyEtherWallet/ethereum-lists/master/src/icons/ETH-0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.png"

    private var lastSentryFailureText = ""

    fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        val deviceInfo: String
        val manufacturerLength = if (TextUtils.isEmpty(manufacturer)) 0 else manufacturer.length
        val manufacturerFromModel = if (model.length > manufacturerLength)
            TextUtils.substring(model, 0, manufacturerLength)
        else
            ""
        deviceInfo = if (TextUtils.equals(manufacturer, manufacturerFromModel)) {
            model
        } else {
            "$manufacturer $model"
        }
        return deviceInfo
    }

    private fun getSamsungBlockchainStatusInfo() = if (Preferences.main.getStorageType() == KeysStorageType.SAMSUNG) {
        ", Samsung Blockchain v. " + SamsungBlockchainUtils.getApiLevel() + " linked"
    } else {
        ""
    }

    fun getCountryIso(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val networkCountryIso = telephonyManager.networkCountryIso
        return if (networkCountryIso.isEmpty()) {
            getSystemLocale().country
        } else {
            networkCountryIso
        }
    }

    fun getReferenceId(anonymousId: String): String {
        val market = if (Preferences.main.getStorageType() == KeysStorageType.SAMSUNG) "AndroidGalaxy" else "AndroidPlay"
        return "$market|$anonymousId"
    }

    fun getSystemLanguage() = when (getSystemLocale().language) {
        Locale("ru").language -> AppLanguage.RUSSIAN
        else -> AppLanguage.ENGLISH
    }

    fun getCurrency(): AppCurrency {
        val currency = try {
            Currency.getInstance(Locale.getDefault())
        } catch (e: Exception) {
            Currency.getInstance(Locale.US)
        }
        return when (currency.currencyCode) {
            "EUR" -> AppCurrency.EUR
            "RUB" -> AppCurrency.RUB
            else -> AppCurrency.USD
        }
    }

    fun getCurrencyAndRate(): Pair<AppCurrency, BigDecimal> {
        val currentCurrency = Preferences.persistent.getAppCurrency()
        val savedCurrency = Database.instance.getCurrencyDao().get(currentCurrency.name)?.exchangeRate
        if (savedCurrency == null) {
            return AppCurrency.getDefault()
        } else {
            return Pair(currentCurrency, savedCurrency)
        }
    }

    fun getSystemLocale(): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Resources.getSystem().configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            return Resources.getSystem().configuration.locale
        }
    }

    fun getAppInstallTime(context: Context) = try {
        context
            .packageManager
            .getPackageInfo(context.packageName, 0)
            .firstInstallTime
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val componentName = intent?.component
        val mainIntent = Intent.makeRestartActivityTask(componentName)
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    fun sendFailureToSentry(activity: Activity?, failure: Failure, hint: String) {
        val message = failure.throwable?.let { throwable ->
            try {
                val errorBody = (throwable as HttpException)
                    .response()!!
                    .errorBody()!!
                    .string()
                JSONObject(errorBody).getString("msg")
            } catch (e: Exception) {
                MewLog.w(TAG, "Unable to retrieve server error", e)
                throwable.message
            }
        } ?: "Empty throwable"
        val hintWithMessage = "$hint [$message]"
        MewLog.e(TAG, hintWithMessage, failure.throwable)
        if (lastSentryFailureText != hintWithMessage) {
            lastSentryFailureText = hintWithMessage
            sendMessageToSentry(activity, hintWithMessage)
        }
    }

    fun sendExceptionToSentry(activity: Activity?, throwable: Throwable, hint: String? = null) {
        if (activity is AppActivityImpl) {
            activity.sendExceptionToSentry(throwable, hint)
        }
    }

    fun sendMessageToSentry(activity: Activity?, report: String) {
        if (activity is AppActivityImpl) {
            activity.sendMessageToSentry(report)
        }
    }

    fun fetchAccentColor(context: Context): Int {
        val typedArray: TypedArray = context.obtainStyledAttributes(intArrayOf(R.attr.colorAccent))
        val color = typedArray.getColor(0, 0)
        typedArray.recycle()
        return color
    }
}
