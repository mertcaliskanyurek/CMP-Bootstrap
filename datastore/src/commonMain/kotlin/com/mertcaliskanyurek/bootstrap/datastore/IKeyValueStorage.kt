package com.mertcaliskanyurek.bootstrap.datastore

import kotlinx.coroutines.flow.Flow

interface IKeyValueStorage {
    fun <T> observe(key: StorageKey<T>): Flow<T?>
    suspend fun <T> get(key: StorageKey<T>): T?
    suspend fun <T> put(key: StorageKey<T>, value: T)
    suspend fun <T> remove(key: StorageKey<T>)
    suspend fun clear()
}
