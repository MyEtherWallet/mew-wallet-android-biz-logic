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
    fun insertOrReplace(item: T): Long

    @Insert
    fun insert(obj: T): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(item: T): Long

    @Insert
    fun insert(vararg obj: T): LongArray

    @Insert
    fun insert(obj: List<T>): LongArray

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrIgnore(obj: List<T>): LongArray

    @Update
    fun update(obj: T)

    @Delete
    fun delete(obj: T)
}
