package com.mertcaliskanyurek.bootstrap.datastore

import kotlinx.coroutines.flow.Flow

class KeyValueStorage(private val storage: IKeyValueStorage) {
    fun <T> observe(key: StorageKey<T>): Flow<T?> = storage.observe(key)
    suspend fun <T> get(key: StorageKey<T>): T? = storage.get(key)
    suspend fun <T> put(key: StorageKey<T>, value: T) = storage.put(key, value)
    suspend fun <T> remove(key: StorageKey<T>) = storage.remove(key)
    suspend fun clear() = storage.clear()

    companion object {
        fun create(
            context: DataStoreContext,
            config: KeyValueStorageConfig = KeyValueStorageConfig()
        ): KeyValueStorage {
            val factory = KeyValueStorageFactory.default()
            return KeyValueStorage(factory.create(context, config))
        }
    }
}
