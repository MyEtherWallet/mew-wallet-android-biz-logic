package com.myetherwallet.mewwalletbl.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities


/**
 * Created by BArtWell on 23.07.2019.
 */

object NetworkHandler {

    private var connectivityManager: ConnectivityManager? = null

    fun init(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    fun isNetworkConnected(): Boolean {
        // if connectivityManager is not initialized, the test is running
        return connectivityManager?.let { connectivityManager ->
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            when {
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } ?: true
    }

//    fun isNetworkConnected(): Boolean {
////         if connectivityManager is not initialized, the test is running
//        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: return true
//    }
}