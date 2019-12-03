package com.myetherwallet.mewwalletbl.data.database

import com.myetherwallet.mewwalletkit.bip.bip44.Address

data class TokensCount(
    val address: Address,
    val count: Int
)