package com.myetherwallet.mewwalletbl.core.api

import com.myetherwallet.mewwalletbl.data.Balance
import com.myetherwallet.mewwalletbl.data.JsonRpcResponse
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import com.myetherwallet.mewwalletkit.core.extension.hexToByteArray
import java.math.BigInteger

/**
 * Created by BArtWell on 13.09.2019.
 */

class JsonRpcResponseConverter(private val jsonRpcResponse: JsonRpcResponse<String>) {

    private val endOfDataIndex = jsonRpcResponse.getOrThrow().lastIndexOf("01")
    private var offset = endOfDataIndex

    fun toBigInteger() = jsonRpcResponse.getOrThrow().hexToBigInteger()

    fun toInt() = jsonRpcResponse.getOrThrow().hexToBigInteger().toInt()

    fun toWalletBalance(): BigInteger = jsonRpcResponse.getOrThrow().hexToBigInteger()

    fun toBalancesList(): List<Balance> {
        val balances = mutableListOf<Balance>()
        val tokensCount = getInt(getNextBytes(32))
        val hasName = getBoolean(getNextBytes(1))
        val hasWebsite = getBoolean(getNextBytes(1))
        val hasEmail = getBoolean(getNextBytes(1))
        for (i in 0 until tokensCount) {
            // balance
            val balanceOffset = offset - sizeHex(16 + 20 + 1 + 32)
            val balanceStr = jsonRpcResponse.getOrThrow().substring(balanceOffset, balanceOffset + sizeHex(32))
            val balance = getBigInteger(balanceStr)
            if (balance <= BigInteger.ZERO) {
                offset -= sizeHex(16 + 20 + 1 + 32)
                if (hasName) {
                    offset -= sizeHex(16)
                }
                if (hasWebsite) {
                    offset -= sizeHex(32)
                }
                if (hasEmail) {
                    offset -= sizeHex(32)
                }
                continue
            }

            // symbol
            offset -= sizeHex(16)
            val symbolStr = jsonRpcResponse.getOrThrow().substring(offset, offset + sizeHex(16))
            val symbol = getString(symbolStr)

            //addr
            offset -= sizeHex(20)
            val addrStr = jsonRpcResponse.getOrThrow().substring(offset, offset + sizeHex(20))
            val address = "0x$addrStr"

            //decimal
            offset -= sizeHex(1)
            val decimalStr = jsonRpcResponse.getOrThrow().substring(offset, offset + sizeHex(1))
            val decimals = getInt(decimalStr)

            //balance
            offset -= sizeHex(32)

            var name: String? = null
            if (hasName) {
                offset -= sizeHex(16)
                val nameStr = jsonRpcResponse.getOrThrow().substring(offset, offset + sizeHex(16))
                name = getString(nameStr)
            }

            var website: String? = null
            if (hasWebsite) {
                offset -= sizeHex(32)
                val webSiteStr = jsonRpcResponse.getOrThrow().substring(offset, offset + sizeHex(32))
                website = getString(webSiteStr)
            }

            var email: String? = null
            if (hasEmail) {
                offset -= sizeHex(32)
                val emailStr = jsonRpcResponse.getOrThrow().substring(offset, offset + sizeHex(32))
                email = getString(emailStr)
            }

            balances.add(Balance(balance, decimals, symbol, address, name, website, email))
        }
        return balances
    }

    private fun getBigInteger(string: String) = BigInteger(string, 16)

    private fun sizeHex(size: Int) = size * 2

    private fun getNextBytes(bytesCount: Int): String {
        val end = offset
        offset -= sizeHex(bytesCount)
        val start = offset
        return jsonRpcResponse.getOrThrow().substring(start, end)
    }

    private fun getBoolean(string: String) = (trimInt(string) == "1")

    private fun getInt(string: String): Int {
        val trimmed = trimInt(string)
        if (trimmed.isEmpty()) {
            return 0
        }
        return trimmed.toInt(16)
    }

    private fun getString(string: String) = String(string.hexToByteArray()).trimEnd(0.toChar())

    private fun trimInt(string: String) = string.trimStart('0')
}
