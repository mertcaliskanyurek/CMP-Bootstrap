package com.mertcaliskanyurek.bootstrap.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class DataStoreKeyValueStorage(
    private val dataStore: DataStore<Preferences>
) : IKeyValueStorage {

    @Suppress("UNCHECKED_CAST")
    private fun <T> preferencesKey(key: StorageKey<T>): Preferences.Key<T> = when (key) {
        is StorageKey.StringKey -> stringPreferencesKey(key.name)
        is StorageKey.IntKey -> intPreferencesKey(key.name)
        is StorageKey.BooleanKey -> booleanPreferencesKey(key.name)
        is StorageKey.LongKey -> longPreferencesKey(key.name)
        is StorageKey.FloatKey -> floatPreferencesKey(key.name)
        is StorageKey.DoubleKey -> doublePreferencesKey(key.name)
    } as Preferences.Key<T>

    override fun <T> observe(key: StorageKey<T>): Flow<T?> =
        dataStore.data.map { it[preferencesKey(key)] }

    override suspend fun <T> get(key: StorageKey<T>): T? =
        observe(key).first()

    override suspend fun <T> put(key: StorageKey<T>, value: T) {
        dataStore.edit { it[preferencesKey(key)] = value }
    }

    override suspend fun <T> remove(key: StorageKey<T>) {
        dataStore.edit { it.remove(preferencesKey(key)) }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}
