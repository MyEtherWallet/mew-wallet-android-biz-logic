package com.myetherwallet.mewwalletbl.core.api.ws.node.data

import com.myetherwallet.mewwalletbl.data.BlockResponse
import com.myetherwallet.mewwalletkit.core.extension.hexToBigInteger
import java.math.BigInteger

/**
 * Created by BArtWell on 19.02.2022.
 */

data class GasInfo(
    val baseFeePerGas: BigInteger,
    val tip: BigInteger
) {
    fun getGasPrice() = baseFeePerGas + tip
}

fun GasInfo(newHeads: EventNewHeads, gasPrice: BigInteger) = createGasInfo(newHeads.baseFeePerGas, gasPrice)

fun GasInfo(blockResponse: BlockResponse, gasPrice: BigInteger) = createGasInfo(blockResponse.baseFeePerGas.hexToBigInteger(), gasPrice)

private fun createGasInfo(baseFeePerGas: BigInteger, gasPrice: BigInteger): GasInfo {
    val tip = gasPrice - baseFeePerGas
    return GasInfo(baseFeePerGas, tip)
}
