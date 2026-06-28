package com.mertcaliskanyurek.cmpbootstrap.presentation

/**
 * Interface for UI states that support loading and error states.
 */
interface LoadableState {
    val isLoading: Boolean
    val error: UiError?
}
