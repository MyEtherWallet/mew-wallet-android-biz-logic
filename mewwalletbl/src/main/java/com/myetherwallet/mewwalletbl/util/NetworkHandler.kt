package com.myetherwallet.mewwalletbl.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/**
 * Created by BArtWell on 23.07.2019.
 */

object NetworkHandler {

    private lateinit var connectivityManager: ConnectivityManager

    fun init(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isNetworkConnected(): Boolean {
        connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        }
        return false
    }
}