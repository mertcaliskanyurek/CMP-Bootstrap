package com.mertcaliskanyurek.bootstrap.networking

import com.mertcaliskanyurek.cmpbootstrap.domain.AppError
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException

/**
 * Maps network-related exceptions to domain-specific [AppError].
 */
fun Throwable.toAppError(): AppError {
    return when (this) {
        is AppError -> this
        is ClientRequestException -> {
            if (response.status.value == 401) {
                AppError.Unauthorized()
            } else {
                AppError.Network(
                    code = response.status.value,
                    message = message ?: "Client error"
                )
            }
        }
        is ServerResponseException -> AppError.Server(
            message = "Server error: ${response.status.value}"
        )
        is RedirectResponseException -> AppError.Network(
            code = response.status.value,
            message = "Redirect error"
        )
        is IOException -> AppError.NoInternet()
        is CancellationException -> throw this
        else -> AppError.Unexpected(this)
    }
}

/**
 * Executes the given [block] and maps any thrown exceptions to [AppError].
 */
suspend fun <T> mapNetworkErrors(block: suspend () -> T): T {
    return try {
        block()
    } catch (e: Throwable) {
        throw e.toAppError()
    }
}
