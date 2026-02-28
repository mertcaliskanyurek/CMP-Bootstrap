package com.mertcaliskanyurek.bootstrap.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal actual fun createPlatformDataStore(
    context: DataStoreContext,
    fileName: String
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            context.context.filesDir.resolve("$fileName.preferences_pb").absolutePath.toPath()
        }
    )
}
