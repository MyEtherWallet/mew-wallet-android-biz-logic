package com.myetherwallet.mewwalletbl.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.data.Blockchain
import com.myetherwallet.mewwalletbl.data.api.Token
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 17.09.2019.
 */

@Entity(tableName = "tokens_descriptions", indices = [Index(value = ["address","blockchain"], unique = true)])
open class EntityTokenDescription(
    val address: Address,
    val decimals: Int,
    val name: String,
    val symbol: String,
    val logo: String?,
    val blockchain: Blockchain
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(token: Token, blockchain: Blockchain) : this(
        if (token.address == Address.DEFAULT_API_CONTRACT) Address.createDefault() else Address.createRaw(token.address),
        token.decimals,
        token.name,
        token.symbol,
        token.icon,
        blockchain
    )

    constructor(swapToken: SwapTokenDescription) : this(
        swapToken.contractAddress,
        swapToken.decimals,
        swapToken.name,
        swapToken.symbol,
        swapToken.icon,
        swapToken.blockchain
    )
}
