package com.myetherwallet.mewwalletbl.extension

import com.myetherwallet.mewwalletbl.core.MewLog
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException

private const val TAG = "Throwable"

fun Throwable.getMessage(): String? {
    try {
        if (this is HttpException) {
            this.response()?.errorBody()?.string()?.let {
                var message: String? = null
                if (it.isNotEmpty()) {
                    try {
                        val jsonObject = JSONObject(it)
                        message = when {
                            jsonObject.has("msg") -> jsonObject.get("msg") as String
                            jsonObject.has("messages") -> jsonObject.getJSONArray("messages").getString(0)
                            jsonObject.has("error") -> {
                                val error = jsonObject.get("error")
                                if (error is JSONObject) {
                                    error.getString("message")
                                } else {
                                    error as String
                                }
                            }
                            jsonObject.has("message") -> jsonObject.get("message") as String
                            else -> null
                        }
                    } catch (e: JSONException) {
                        MewLog.d(TAG, "Unable to parse error log", e)
                    }
                }
                return "[" + this.code() + "] " + (message ?: this.message())
            }
        } else {
            return this::class.java.name + ": " + this.message
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}
