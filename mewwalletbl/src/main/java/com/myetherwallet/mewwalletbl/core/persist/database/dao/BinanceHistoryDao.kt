package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.Blockchain
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
            "create_time AS updatedAt," +
            "deposit_amount AS depositAmount," +
            "swap_amount AS swapAmount," +
            "deposit_received_confirms AS depositReceivedConfirms," +
            "deposit_hash AS depositHash," +
            "swap_hash AS swapHash," +
            "exchange_gas_amount AS exchangeGasAmount," +
            "token_per_bnb AS tokenPerBNB," +
            "${PricesDao.TABLE_NAME}.price AS fiatPrice " +
            "FROM $TABLE_NAME INNER JOIN ${TokenDescriptionDao.TABLE_NAME} " +
            "ON ${TokenDescriptionDao.TABLE_NAME}.blockchain=:blockchain AND ${TokenDescriptionDao.TABLE_NAME}.address=CASE WHEN $TABLE_NAME.eth_contract_address ='0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee' THEN '' ELSE $TABLE_NAME.eth_contract_address END " +
            "LEFT JOIN ${PricesDao.TABLE_NAME} " +
            "ON ${PricesDao.TABLE_NAME}.tokenId=${TokenDescriptionDao.TABLE_NAME}.id")
    abstract fun getAll(blockchain: Blockchain): List<BinanceStatus>

    @Query("SELECT * FROM $TABLE_NAME WHERE id_key=:id")
    abstract fun get(id: String): EntityBinanceHistory?

    @Query("DELETE FROM $TABLE_NAME WHERE id_key=:id")
    abstract fun delete(id: String)

    @Query("UPDATE $TABLE_NAME SET deposit_hash=:txHash WHERE id_key=:id")
    abstract fun saveDepositHash(id: String, txHash: String)

    companion object {
        const val TABLE_NAME = "binance_history"
    }
}
