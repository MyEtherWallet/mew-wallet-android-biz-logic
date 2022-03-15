package com.myetherwallet.mewwalletbl.core

/**
 * Created by BArtWell on 20.04.2020.
 */

class LogsCollector {

    private val buffer = StringBuilder()

    fun add(tag: String, msg: String) {
        MewLog.d(tag, msg)
        buffer.append("$tag: $msg")
        buffer.append("\n")
    }

    fun add(tag: String, msg: String, e: Throwable?) {
        MewLog.d(tag, msg, e)
        e?.let {
            buffer.append("$tag: $msg\n" + e.stackTrace.joinToString("\n"))
        } ?: buffer.append("No stacktrace, throwable is null")
        buffer.append("\n")
    }

    fun getAll() = buffer.toString()
}
