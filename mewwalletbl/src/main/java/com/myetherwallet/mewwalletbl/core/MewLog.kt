package com.myetherwallet.mewwalletbl.core

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast

/**
 * Created by BArtWell on 18.07.2019.
 */

object MewLog {

    internal fun shouldDisplayLogs() = true
    private fun canDisplayToasts() = false

    fun v(tag: String, msg: String) {
        if (shouldDisplayLogs()) {
            Log.v(tag, msg)
        }
    }


    fun v(tag: String, msg: String, tr: Throwable) {
        if (shouldDisplayLogs()) {
            Log.v(tag, msg, tr)
        }
    }

    fun d(tag: String, msg: String) {
        if (shouldDisplayLogs()) {
            Log.d(tag, msg)
        }
    }

    fun d(tag: String, msg: String, tr: Throwable?) {
        if (shouldDisplayLogs()) {
            Log.d(tag, msg, tr)
        }
    }

    fun i(tag: String, msg: String) {
        if (shouldDisplayLogs()) {
            Log.i(tag, msg)
        }
    }

    fun i(tag: String, msg: String, tr: Throwable?) {
        if (shouldDisplayLogs()) {
            Log.i(tag, msg, tr)
        }
    }

    fun w(tag: String, msg: String) {
        if (shouldDisplayLogs()) {
            Log.w(tag, msg)
        }
    }

    fun w(tag: String, msg: String, tr: Throwable?) {
        if (shouldDisplayLogs()) {
            Log.w(tag, msg, tr)
        }
    }

    fun e(tag: String, msg: String) {
        if (shouldDisplayLogs()) {
            Log.e(tag, msg)
        }
    }

    fun e(tag: String, msg: String, tr: Throwable?) {
        if (shouldDisplayLogs()) {
            Log.e(tag, msg, tr)
        }
    }

    fun showDebugToast(context: Context, text: String) {
        if (canDisplayToasts()) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
            }
        }
    }
}