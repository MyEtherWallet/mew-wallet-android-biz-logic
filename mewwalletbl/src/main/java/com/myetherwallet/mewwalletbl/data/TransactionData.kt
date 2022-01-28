package com.myetherwallet.mewwalletbl.data

import com.myetherwallet.mewwalletkit.core.extension.addHexPrefix
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import com.myetherwallet.mewwalletkit.core.extension.toHexString
import java.math.BigInteger
import java.util.*

/**
 * Created by BArtWell on 30.07.2019.
 */

data class TransactionData(
    val function: String,
    val address: String,
    val amount: BigInteger
) {

    companion object {
        const val FUNCTION_TOKEN_TRANSFER = "0xa9059cbb"
        const val FUNCTION_APPROVAL = "0x095ea7b3"
        const val FUNCTION_DEPOSIT = "0xe8eda9df"
        const val FUNCTION_BORROW = "0xa415bcad"
        const val FUNCTION_WITHDRAW = "0x69328dec"

        fun create(data: ByteArray): TransactionData? {
            val dataHex = data.toHexString().addHexPrefix()
            if (dataHex.length < 10) {
                return null
            }
            val function = dataHex.substring(0, 10)
            return when (function) {
                FUNCTION_TOKEN_TRANSFER -> {
                    TransactionData(
                        function.lowercase(Locale.US),
                        dataHex.substring(34, 74).addHexPrefix(),
                        dataHex.substring(74, 138).hexToBigInteger()
                    )
                }

                FUNCTION_APPROVAL -> {
                    TransactionData(
                        function.lowercase(Locale.US),
                        dataHex.substring(34, 74).addHexPrefix(),
                        dataHex.substring(74, 138).hexToBigInteger()
                    )
                }

                FUNCTION_DEPOSIT -> {
                    TransactionData(
                        function.lowercase(Locale.US),
                        dataHex.substring(34, 74).addHexPrefix(),
                        dataHex.substring(74, 138).hexToBigInteger()
                    )
                }

                FUNCTION_BORROW -> {
                    TransactionData(
                        function.lowercase(Locale.US),
                        dataHex.substring(34, 74).addHexPrefix(),
                        dataHex.substring(74, 138).hexToBigInteger()
                    )
                }

                FUNCTION_WITHDRAW -> {
                    TransactionData(
                        function.lowercase(Locale.US),
                        dataHex.substring(34, 74).addHexPrefix(),
                        dataHex.substring(74, 138).hexToBigInteger()
                    )
                }

                else -> null
            }
        }
    }
}
