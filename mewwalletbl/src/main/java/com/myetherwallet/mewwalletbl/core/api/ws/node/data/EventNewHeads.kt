package com.myetherwallet.mewwalletbl.core.api.ws.node.data

import java.math.BigInteger

/**
 * Created by BArtWell on 15.02.2022.
 */

data class EventNewHeads(
    val number: BigInteger,
    val gasLimit: BigInteger,
    val baseFeePerGas: BigInteger
)
