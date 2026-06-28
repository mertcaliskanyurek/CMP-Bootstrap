package com.mertcaliskanyurek.bootstrap.datastore

import com.mertcaliskanyurek.cmpbootstrap.domain.AppError
import androidx.datastore.core.IOException

/**
 * Maps DataStore-related exceptions to domain-specific [AppError].
 */
fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppError -> this
        is IOException -> AppError.Storage(
            message = "Failed to access storage",
            cause = this,
        )
        else -> AppError.Unexpected(this)
    }
}

/**
 * Executes the given [block] and maps any thrown exceptions to [AppError].
 */
suspend fun <T> mapStorageErrors(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: Throwable) {
        throw e.toAppError()
    }
}
