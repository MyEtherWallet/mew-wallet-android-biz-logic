package com.myetherwallet.mewwalletbl.core.persist.database

import android.content.Context
import androidx.room.Room

/**
 * Created by BArtWell on 18.09.2019.
 */

object Database {

    lateinit var instance: MewDatabase

    internal fun init(context: Context) {
        instance = Room.databaseBuilder(context, MewDatabase::class.java, "db_mew").build()
    }

    fun drop(context: Context) {
        context.deleteDatabase("db_mew")
        init(context)
    }
}
