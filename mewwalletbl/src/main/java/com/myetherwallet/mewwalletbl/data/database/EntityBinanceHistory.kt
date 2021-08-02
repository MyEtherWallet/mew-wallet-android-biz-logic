package com.myetherwallet.mewwalletbl.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.myetherwallet.mewwalletbl.core.persist.database.dao.BinanceHistoryDao
import com.myetherwallet.mewwalletbl.data.api.binance.BinanceStatus
import com.myetherwallet.mewwalletbl.data.api.binance.MoveStatus
import com.myetherwallet.mewwalletkit.bip.bip44.Address
import java.math.BigDecimal
import java.util.*

@Entity(tableName = BinanceHistoryDao.TABLE_NAME, indices = [Index(value = ["id_key"], unique = true)])
class EntityBinanceHistory(
    val address: Address,
    @ColumnInfo(name = "id_key")
    val idKey: String,
    val status: MoveStatus,
    val symbol: String,
    val icon: String?,
    val amount: BigDecimal,
    @ColumnInfo(name = "from_network")
    val fromNetwork: String,
    @ColumnInfo(name = "to_network")
    val toNetwork: String,
    @ColumnInfo(name = "from_address")
    val fromAddress: Address,
    @ColumnInfo(name = "to_address")
    val toAddress: Address,
    @ColumnInfo(name = "deposit_address")
    val depositAddress: Address,
    @ColumnInfo(name = "eth_contract_address")
    val ethContractAddress: Address,
    @ColumnInfo(name = "bsc_contract_address")
    val bscContractAddress: Address,
    @ColumnInfo(name = "eth_contract_decimal")
    val ethContractDecimal: Int,
    @ColumnInfo(name = "bsc_contract_decimal")
    val bscContractDecimal: Int,
    @ColumnInfo(name = "swap_fee")
    val swapFee: BigDecimal,
    @ColumnInfo(name = "swap_fee_rate")
    val swapFeeRate: BigDecimal,
    @ColumnInfo(name = "network_fee")
    val networkFee: BigDecimal,
    @ColumnInfo(name = "deposit_timeout")
    val depositTimeout: Date,
    @ColumnInfo(name = "deposit_required_confirms")
    val depositRequiredConfirms: String,
    @ColumnInfo(name = "create_time")
    val createTime: Date,
    @ColumnInfo(name = "deposit_amount")
    val depositAmount: BigDecimal,
    @ColumnInfo(name = "swap_amount")
    val swapAmount: BigDecimal,
    @ColumnInfo(name = "deposit_received_confirms")
    val depositReceivedConfirms: String,
    @ColumnInfo(name = "deposit_hash")
    val depositHash: String,
    @ColumnInfo(name = "swap_hash")
    val swapHash: String,
    @ColumnInfo(name = "exchange_gas_amount")
    val exchangeGasAmount: BigDecimal,
    @ColumnInfo(name = "token_per_bnb")
    val tokenPerBNB: BigDecimal
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    constructor(binanceStatus: BinanceStatus) : this(
        binanceStatus.address,
        binanceStatus.id,
        binanceStatus.status,
        binanceStatus.symbol,
        binanceStatus.icon,
        binanceStatus.amount,
        binanceStatus.fromNetwork,
        binanceStatus.toNetwork,
        binanceStatus.fromAddress,
        binanceStatus.toAddress,
        binanceStatus.depositAddress,
        binanceStatus.ethContractAddress,
        binanceStatus.bscContractAddress,
        binanceStatus.ethContractDecimal,
        binanceStatus.bscContractDecimal,
        binanceStatus.swapFee,
        binanceStatus.swapFeeRate,
        binanceStatus.networkFee,
        binanceStatus.depositTimeout,
        binanceStatus.depositRequiredConfirms,
        binanceStatus.createTime,
        binanceStatus.depositAmount,
        binanceStatus.swapAmount,
        binanceStatus.depositReceivedConfirms,
        binanceStatus.depositHash,
        binanceStatus.swapHash,
        binanceStatus.exchangeGasAmount,
        binanceStatus.tokenPerBNB
    )
}
