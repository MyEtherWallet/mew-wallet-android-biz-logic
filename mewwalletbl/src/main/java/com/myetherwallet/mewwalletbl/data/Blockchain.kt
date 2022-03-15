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
        "https://etherscan.io/",
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
        "https://bscscan.com/",
        "https://raw.githubusercontent.com/MyEtherWallet/ethereum-lists/master/src/icons/BNB-0xB8c77482e45F1F44dE1745F52C74426C631bDD52-eth.png"
    ),
    MATIC(
        "Polygon",
        "MATIC",
        Color.parseColor("#8247E5"),
        "ERC-20",
        "matic",
        137,
        "MATIC",
        "https://polygonscan.com/",
        "https://raw.githubusercontent.com/MyEtherWallet/ethereum-lists/master/src/icons/MATIC-0x7D1AfA7B718fb893dB30A3aBc0Cfc608AaCfeBB0-eth.png"
    );

    companion object {

        fun getBlockchain(symbol: String) = values().firstOrNull { symbol == it.symbol } ?: Preferences.persistent.getBlockchain()

        fun getBlockchainOrNull(chainId: Int) = values().firstOrNull { chainId == it.chainId }

    }
}


