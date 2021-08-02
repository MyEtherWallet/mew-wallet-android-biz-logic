package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.myetherwallet.mewwalletbl.data.database.EntityAccount
import com.myetherwallet.mewwalletkit.bip.bip44.Address

/**
 * Created by BArtWell on 18.09.2019.
 */

@Dao
abstract class AccountsDao : BaseDao<EntityAccount> {

    @Query("SELECT * FROM $TABLE_NAME WHERE address=:address COLLATE NOCASE")
    abstract suspend fun get(address: Address): EntityAccount?

    @Query("SELECT * FROM $TABLE_NAME ORDER BY position ASC")
    abstract suspend fun getAll(): List<EntityAccount>

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract suspend fun getCount(): Int

    @Query("SELECT MAX(id) FROM $TABLE_NAME")
    abstract suspend fun getLastId(): Int?

    @Query("INSERT INTO $TABLE_NAME VALUES(null,(SELECT COUNT(*) FROM $TABLE_NAME)+1,:walletId,:address,:name,0,:anonymousId,'',:eth2Address)")
    abstract suspend fun insert(walletId: Long, address: String, name: String, anonymousId: String, eth2Address: String): Long

    @Query("UPDATE $TABLE_NAME SET address=:address WHERE id=:id")
    abstract suspend fun updateAddress(id: Int, address: String)

    @Query("UPDATE $TABLE_NAME SET eth2_address=:eth2Address WHERE id=:id")
    abstract suspend fun updateEth2Address(id: Long, eth2Address: String)

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id LIMIT 1")
    abstract suspend fun getFirst(): EntityAccount?

    companion object {
        const val TABLE_NAME: String = "accounts"
    }
}