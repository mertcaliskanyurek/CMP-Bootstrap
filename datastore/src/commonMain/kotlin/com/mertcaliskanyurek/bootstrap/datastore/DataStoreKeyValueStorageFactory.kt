package com.mertcaliskanyurek.bootstrap.datastore

internal class DataStoreKeyValueStorageFactory : KeyValueStorageFactory {
    override fun create(context: DataStoreContext, config: KeyValueStorageConfig): IKeyValueStorage {
        val dataStore = createPlatformDataStore(context, config.name)
        return DataStoreKeyValueStorage(dataStore)
    }
}
