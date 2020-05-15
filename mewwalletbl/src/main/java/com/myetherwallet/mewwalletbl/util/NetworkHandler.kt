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

//    fun isNetworkConnected(): Boolean {
//        // if connectivityManager is not initialized, the test is running
//        connectivityManager?.let { connectivityManager ->
//            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let { networkCapabilities ->
//                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
//                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
//            }
//            return false
//        } ?: return true
//    }

    fun isNetworkConnected(): Boolean {
        // if connectivityManager is not initialized, the test is running
        return connectivityManager?.activeNetworkInfo?.isConnectedOrConnecting ?: return true
    }
}