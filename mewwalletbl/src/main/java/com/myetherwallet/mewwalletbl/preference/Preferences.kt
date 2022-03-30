package com.myetherwallet.mewwalletbl.preference

import android.content.Context

/**
 * Created by BArtWell on 17.07.2019.
 */

object Preferences {

    lateinit var main: MainPreferences
        private set
    lateinit var wallet: WalletPreferences
        private set
    lateinit var persistent: PersistentPreferences
        private set

    fun init(context: Context) {
        main = MainPreferences(context)
        wallet = WalletPreferences(context)
        persistent = PersistentPreferences(context)
    }
}
