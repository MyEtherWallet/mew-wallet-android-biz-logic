package com.myetherwallet.mewwalletbl

/**
 * Created by BArtWell on 23.09.2020.
 */

interface AppActivityImpl {

    fun sendMessageToSentry(message: String)

    fun sendExceptionToSentry(throwable: Throwable, hint: String? = null)
}
