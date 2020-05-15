package com.myetherwallet.mewwalletbl

import com.myetherwallet.mewwalletbl.preference.Preferences
import com.myetherwallet.mewwalletkit.bip.bip44.Network

/**
 * Created by BArtWell on 24.10.2019.
 */

object MewEnvironment {

    val current = Preferences.main.getDebugEnvironmentType()

    enum class Type(val node: String, val api: String, val network: Network) {
        PRODUCTION("eth", BuildConfig.MEW_MAINNET_API_END_POINT, Network.ETHEREUM),
        DEVELOPMENT("eth", BuildConfig.MEW_DEVELOPMENT_API_END_POINT, Network.ETHEREUM),
        STAGING("eth", BuildConfig.MEW_STAGING_API_END_POINT, Network.ETHEREUM),
        QA("eth", BuildConfig.MEW_QA_API_END_POINT, Network.ETHEREUM),
        ROPSTEN("rop", BuildConfig.MEW_DEVELOPMENT_API_END_POINT, Network.ROPSTEN)
    }
}
