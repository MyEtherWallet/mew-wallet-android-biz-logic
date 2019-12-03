package com.myetherwallet.mewwalletbl.data.database

import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 24.10.2019.
 */

data class Recent(
    val address: Address,
    var name: String? = null
)