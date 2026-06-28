package com.mertcaliskanyurek.cmpbootstrap.domain

/**
 * A sealed class representing domain-specific errors in the application.
 */
sealed class AppError : Exception() {
    data class Network(val code: Int? = null, override val message: String) : AppError()
    data class Server(override val message: String) : AppError()
    data class Storage(override val message: String, override val cause: Throwable? = null) : AppError()
    data class Media(override val message: String, override val cause: Throwable? = null) : AppError()
    class Unauthorized : AppError() {
        override val message: String = "Unauthorized"
    }
    class NoInternet : AppError() {
        override val message: String = "No internet connection"
    }
    data class Unexpected(val e: Throwable) : AppError() {
        override val message: String? = e.message
    }
    
    /**
     * Helper to get a user-friendly message for this error.
     */
    val displayMessage: String
        get() = when (this) {
            is Network -> "Network error ($code): $message"
            is Server -> message
            is Storage -> "Storage error: $message"
            is Media -> "Media error: $message"
            is Unauthorized -> "Session expired. Please login again."
            is NoInternet -> "No internet connection. Please check your settings."
            is Unexpected -> e.message ?: "An unexpected error occurred."
        }
}
