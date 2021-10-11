package com.myetherwallet.mewwalletbl.core.persist.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

/**
 * Created by BArtWell on 18.09.2019.
 */

interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(item: T): Long

    @Insert
    suspend fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(item: T): Long

    @Insert
    suspend fun insert(vararg obj: T): LongArray

    @Insert
    suspend fun insert(obj: List<T>): LongArray

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(obj: List<T>): LongArray

    @Update
    suspend fun update(obj: T)

    @Delete
    suspend fun delete(obj: T)
}
