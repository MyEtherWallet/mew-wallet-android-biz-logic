package com.myetherwallet.mewwalletbl.core

import android.util.Log

/**
 * Created by BArtWell on 20.04.2020.
 */

class LogsCollector {

    private val buffer = StringBuilder()

    fun add(tag: String, msg: String) {
        Log.d(tag, msg)
        buffer.append("$tag: $msg")
        buffer.append("\n")
    }

    fun add(tag: String, msg: String, e: Throwable) {
        Log.d(tag, msg, e)
        buffer.append("$tag: $msg\n" + e.stackTrace.joinToString("\n"))
        buffer.append("\n")
    }

    fun getAll() = buffer.toString()
}
