package com.mertcaliskanyurek.cmpbootstrap.presentation

import com.mertcaliskanyurek.cmpbootstrap.domain.AppError

/**
 * A UI-specific representation of an error, suitable for displaying to the user.
 */
data class UiError(
    val message: String,
    val appError: AppError,
    val onRetry: (() -> Unit)? = null
)

/**
 * Extension to convert [AppError] to [UiError] with an optional retry action.
 */
fun AppError.toUiError(onRetry: (() -> Unit)? = null): UiError {
    return UiError(
        message = this.displayMessage,
        appError = this,
        onRetry = onRetry
    )
}
