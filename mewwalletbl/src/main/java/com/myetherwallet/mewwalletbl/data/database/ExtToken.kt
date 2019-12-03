package com.myetherwallet.mewwalletbl.data.database

import com.myetherwallet.mewwalletkit.bip.bip44.Address

class ExtToken(
    val accountId: Long,
    val tokenDescriptionId: Long,
    val isPrimary: Boolean,
    val address: Address,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?
)