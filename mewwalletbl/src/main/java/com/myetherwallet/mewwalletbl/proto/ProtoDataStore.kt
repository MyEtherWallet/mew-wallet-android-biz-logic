package com.myetherwallet.mewwalletbl.proto

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by BArtWell on 10.03.2022.
 */

class ProtoDataStore<T> private constructor(fileName: String, serializer: Serializer<T>) {

    private val Context.dataStore by dataStore(
        fileName = "$fileName.pb",
        serializer = serializer,
    )

    private lateinit var dataStore: DataStore<T>

    internal fun init(context: Context) {
        dataStore = context.dataStore
        GlobalScope.launch {
            dataStore.data.first()
        }
    }

    fun get(): T = runBlocking { dataStore.data.first() }

    fun save(value: T) {
        runBlocking {
            dataStore.updateData {
                value
            }
        }
    }

    companion object {

        internal fun <T> create(context: Context, fileName: String, serializer: Serializer<T>): ProtoDataStore<T> {
            val protoDataStore = ProtoDataStore(fileName, serializer)
            protoDataStore.init(context)
            return protoDataStore
        }
    }
}
