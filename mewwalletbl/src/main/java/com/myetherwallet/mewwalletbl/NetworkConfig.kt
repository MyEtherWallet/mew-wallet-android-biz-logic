package com.myetherwallet.mewwalletbl

import com.myetherwallet.mewwalletkit.bip.bip44.Network

/**
 * Created by BArtWell on 24.10.2019.
 */

object NetworkConfig {

    val current = Configuration.MAINNET

    enum class Configuration(val node: String, val api: String, val network: Network) {
        MAINNET("eth", BuildConfig.MEW_MAINNET_API_END_POINT, Network.ETHEREUM),
        ROPSTEN("rop", BuildConfig.MEW_ROPSTEN_API_END_POINT, Network.ROPSTEN)
    }
}
