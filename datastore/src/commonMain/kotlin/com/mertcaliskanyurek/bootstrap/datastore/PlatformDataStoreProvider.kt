package com.mertcaliskanyurek.bootstrap.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

internal expect fun createPlatformDataStore(
    context: DataStoreContext,
    fileName: String
): DataStore<Preferences>
