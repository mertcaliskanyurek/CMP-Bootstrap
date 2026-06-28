package com.mertcaliskanyurek.cmpbootstrap.presentation

/**
 * Interface for one-time UI effects (e.g. showing a snackbar, toast, or navigation).
 */
interface UiEffect {
    data class ShowSnackbar(val message: String) : UiEffect
}
