package com.myetherwallet.mewwalletbl.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by BArtWell on 17.01.2022.
 */

open class BasePreferences(context: Context, private val preferencesName: String) {

    private val Context.dataStore by preferencesDataStore(
        name = preferencesName,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, preferencesName))
        }
    )
    private var dataStore: DataStore<Preferences> = context.dataStore

    init {
        GlobalScope.launch {
            dataStore.data.first()
        }
    }

    protected fun getBoolean(key: String, defaultValue: Boolean) = get(booleanPreferencesKey(key), defaultValue)

    protected fun getInt(key: String, defaultValue: Int) = get(intPreferencesKey(key), defaultValue)

    protected fun getLong(key: String, defaultValue: Long) = get(longPreferencesKey(key), defaultValue)

    protected fun getString(key: String, defaultValue: String) = get(stringPreferencesKey(key), defaultValue)

    protected fun getStringOrNull(key: String) = getOrNull(stringPreferencesKey(key))

    private fun <T> get(key: Preferences.Key<T>, defaultValue: T) = getOrNull(key) ?: defaultValue

    private fun <T> getOrNull(key: Preferences.Key<T>): T? {
        val preferences = runBlocking { dataStore.data.first() }
        return preferences[key]
    }

    protected fun getAll(): Map<String, Any> {
        return runBlocking {
            dataStore
                .data
                .first()
                .asMap()
                .mapKeys { it.key.name }
        }
    }

    protected fun getStringLiveData(key: String) = getLiveData(stringPreferencesKey(key))

    private fun <T> getLiveData(key: Preferences.Key<T>): LiveData<T?> {
        return dataStore
            .data
            .catch {
                throw Exception()
            }
            .map {
                it[key]
            }
            .asLiveData()
    }

    protected fun setString(key: String, value: String) = set(stringPreferencesKey(key), value)

    protected fun setBoolean(key: String, value: Boolean) = set(booleanPreferencesKey(key), value)

    protected fun setInt(key: String, value: Int) = set(intPreferencesKey(key), value)

    protected fun setLong(key: String, value: Long) = set(longPreferencesKey(key), value)

    private fun <T> set(key: Preferences.Key<T>, value: T) {
        runBlocking {
            dataStore.edit {
                it[key] = value
            }
        }
    }

    protected fun clear() {
        runBlocking {
            dataStore.edit {
                it.clear()
            }
        }
    }
}
