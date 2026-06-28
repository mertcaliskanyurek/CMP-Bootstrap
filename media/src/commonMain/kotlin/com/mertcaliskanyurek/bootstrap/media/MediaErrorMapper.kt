package com.mertcaliskanyurek.bootstrap.media

import com.mertcaliskanyurek.cmpbootstrap.domain.AppError

/**
 * Maps media-related exceptions to domain-specific [AppError].
 */
fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppError -> this
        else -> AppError.Media(
            message = this.message ?: "Media operation failed",
            cause = this,
        )
    }
}
