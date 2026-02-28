package com.mertcaliskanyurek.bootstrap.datastore

interface KeyValueStorageFactory {
    fun create(context: DataStoreContext, config: KeyValueStorageConfig): IKeyValueStorage

    companion object {
        fun default(): KeyValueStorageFactory = DataStoreKeyValueStorageFactory()
    }
}
