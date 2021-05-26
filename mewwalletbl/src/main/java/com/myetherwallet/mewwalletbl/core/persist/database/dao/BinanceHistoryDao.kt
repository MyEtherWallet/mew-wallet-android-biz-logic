package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.api.binance.BinanceStatus
import com.myetherwallet.mewwalletbl.data.database.EntityBinanceHistory

@Dao
abstract class BinanceHistoryDao : BaseDao<EntityBinanceHistory> {

    @Query("SELECT " +
            "$TABLE_NAME.address," +
            "$TABLE_NAME.id_key AS id," +
            "$TABLE_NAME.status," +
            "$TABLE_NAME.symbol," +
            "$TABLE_NAME.icon," +
            "$TABLE_NAME.amount," +
            "from_network AS fromNetwork," +
            "to_network AS toNetwork," +
            "from_address AS fromAddress," +
            "to_address AS toAddress," +
            "deposit_address AS depositAddress," +
            "eth_contract_address AS ethContractAddress," +
            "bsc_contract_address AS bscContractAddress," +
            "eth_contract_decimal AS ethContractDecimal," +
            "bsc_contract_decimal AS bscContractDecimal," +
            "swap_fee AS swapFee," +
            "swap_fee_rate AS swapFeeRate," +
            "network_fee AS networkFee," +
            "deposit_timeout AS depositTimeout," +
            "deposit_required_confirms AS depositRequiredConfirms," +
            "create_time AS createTime," +
            "deposit_amount AS depositAmount," +
            "swap_amount AS swapAmount," +
            "deposit_received_confirms AS depositReceivedConfirms," +
            "deposit_hash AS depositHash," +
            "swap_hash AS swapHash," +
            "exchange_gas_amount AS exchangeGasAmount," +
            "${PricesDao.TABLE_NAME}.price AS fiatPrice " +
            "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
            "ON ${TokenDescriptionDao.TABLE_NAME}.address=CASE WHEN $TABLE_NAME.eth_contract_address ='0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee' THEN '' ELSE $TABLE_NAME.eth_contract_address END " +
            "LEFT JOIN ${PricesDao.TABLE_NAME} " +
            "ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id")
    abstract fun getAll(): List<BinanceStatus>

    companion object {
        const val TABLE_NAME = "binance_history"
    }
}
