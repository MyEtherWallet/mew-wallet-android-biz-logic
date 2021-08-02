package com.myetherwallet.mewwalletbl.extension

import org.json.JSONObject

/**
 * Created by BArtWell on 23.09.2020.
 */

fun JSONObject.getOrNull(key: String) = try {
    this[key]
} catch (e: Exception) {
    null
}

fun JSONObject.getStringOrNull(key: String) = try {
    this.getString(key)
} catch (e: Exception) {
    null
}

fun JSONObject.getLongOrNull(key: String) = try {
    this.getLong(key)
} catch (e: Exception) {
    null
}
