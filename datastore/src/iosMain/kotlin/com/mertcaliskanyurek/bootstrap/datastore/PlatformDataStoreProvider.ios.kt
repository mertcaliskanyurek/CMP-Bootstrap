package com.mertcaliskanyurek.bootstrap.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
internal actual fun createPlatformDataStore(
    context: DataStoreContext,
    fileName: String
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            val dir = NSFileManager.defaultManager.URLForDirectory(
                NSDocumentDirectory, NSUserDomainMask, null, false, null
            )
            requireNotNull(dir?.path) { "iOS document directory unavailable" }
                .let { "$it/$fileName.preferences_pb".toPath() }
        }
    )
}
