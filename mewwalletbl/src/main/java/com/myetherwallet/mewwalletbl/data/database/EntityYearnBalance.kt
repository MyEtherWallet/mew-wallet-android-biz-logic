package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.YearnBalanceDao
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnBalance
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal

@Entity(tableName = YearnBalanceDao.TABLE_NAME, indices = [Index(value = ["account_address","contract_address"], unique = true)])
class EntityYearnBalance(
    @ColumnInfo(name = "account_address")
    val accountAddress: Address,
    @ColumnInfo(name = "contract_address")
    val contractAddress: Address,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "price")
    val price: BigDecimal,
    @ColumnInfo(name = "amount")
    val amount: BigDecimal,
    @ColumnInfo(name = "profit")
    val profit: BigDecimal,
    @ColumnInfo(name = "decimals")
    val decimals: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(accountAddress: Address, data: YearnBalance) : this(
        accountAddress,
        data.contract,
        data.token,
        data.icon,
        data.price,
        data.getAmount(),
        data.getProfit(),
        data.decimals
    )
}