package com.myetherwallet.mewwalletbl.data

import android.graphics.Color
import androidx.annotation.ColorInt
import com.myetherwallet.mewwalletbl.preference.Preferences

/**
 * Created by BArtWell on 30.05.2021.
 */

enum class Blockchain(
    val title: String,
    val symbol: String,
    @ColorInt val color: Int,
    val protocol: String,
    val nodePrefix: String,
    val chainId: Int,
    val token: String,
    val chainExplorer: String,
    val icon: String
) {

    ETHEREUM(
        "Ethereum",
        "ETH",
        Color.parseColor("#05c0a5"),
        "ERC-20",
        "eth",
        1,
        "ETH",
        "https://etherscan.io/tx/",
        "https://raw.githubusercontent.com/MyEtherWallet/ethereum-lists/master/src/icons/ETH-0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee.png"
    ),
    BSC(
        "Binance Smart Chain",
        "BSC",
        Color.parseColor("#f0b90b"),
        "BEP-20",
        "bsc",
        56,
        "BNB",
        "https://bscscan.com/tx/",
        "https://raw.githubusercontent.com/MyEtherWallet/ethereum-lists/master/src/icons/BNB-0xb8c77482e45f1f44de1745f52c74426c631bdd52-eth.png"
    ),
    MATIC(
        "Polygon",
        "MATIC",
        Color.parseColor("#8247E5"),
        "ERC-20",
        "matic",
        137,
        "MATIC",
        "https://polygonscan.com/tx/",
        "https://raw.githubusercontent.com/MyEtherWallet/ethereum-lists/master/src/icons/MATIC-0x7d1afa7b718fb893db30a3abc0cfc608aacfebb0-eth.png"
    );

    companion object {
        fun getBlockchain(symbol: String) = values().firstOrNull { symbol == it.symbol } ?: Preferences.persistent.getBlockchain()
    }
}


