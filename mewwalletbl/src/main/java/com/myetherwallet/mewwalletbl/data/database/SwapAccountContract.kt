package com.myetherwallet.mewwalletbl.data.database

import com.myetherwallet.mewwalletkit.bip.bip44.Address

class SwapAccountContract (
    val accountId: Long,
    val toContract: Address
)