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

    @Query("SELECT * FROM $TABLE_NAME WHERE address=:address")
    abstract fun get(address: Address): EntityAccount?

    @Query("SELECT * FROM $TABLE_NAME ORDER BY position ASC")
    abstract fun getAll(): List<EntityAccount>

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    abstract fun getCount(): Int

    @Query("SELECT MAX(id) FROM $TABLE_NAME")
    abstract fun getLastId(): Int?

    @Query("INSERT INTO $TABLE_NAME VALUES(null,(SELECT COUNT(*) FROM $TABLE_NAME)+1,:walletId,:address,:name,0,:anonymousId,'')")
    abstract fun insert(walletId: Long, address: String, name: String, anonymousId: String): Long

    @Query("UPDATE $TABLE_NAME SET address=:address WHERE id=:id")
    abstract fun updateAddress(id: Int, address: String)

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id LIMIT 1")
    abstract fun getFirst(): EntityAccount?

    companion object {
        const val TABLE_NAME: String = "accounts"
    }
}