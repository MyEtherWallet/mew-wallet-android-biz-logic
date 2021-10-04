package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.myetherwallet.mewwalletbl.data.api.yearn.YearnBalance
import com.myetherwallet.mewwalletbl.data.database.EntityYearnBalance
import com.myetherwallet.mewwalletkit.bip.bip44.Address

@Dao
abstract class YearnBalanceDao : BaseDao<EntityYearnBalance> {

    @Query("SELECT * FROM $TABLE_NAME")
    abstract suspend fun getAll() : List<EntityYearnBalance>

    @Query("SELECT * FROM $TABLE_NAME WHERE account_address=:account")
    abstract suspend fun get(account: Address) : List<EntityYearnBalance>

    @Query("DELETE FROM $TABLE_NAME WHERE account_address=:account")
    abstract suspend fun delete(account: Address)

    @Transaction
    open suspend fun update(accountAddress: Address, list: List<YearnBalance>) {
        delete(accountAddress)
        list.forEach { insertOrReplace(EntityYearnBalance(accountAddress, it)) }
    }

    companion object {
        const val TABLE_NAME: String = "yearn_balance"
    }
}