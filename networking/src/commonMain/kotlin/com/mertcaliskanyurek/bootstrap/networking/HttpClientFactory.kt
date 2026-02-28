package com.mertcaliskanyurek.bootstrap.networking

/**
 * Factory interface for creating HTTP client instances.
 *
 * This abstraction allows different implementations (Ktor, OkHttp, etc.)
 * without exposing them to the client code.
 */
interface HttpClientFactory {
    fun create(config: HttpClientConfig): IHttpClient

    companion object {
        /**
         * Get the default HttpClientFactory implementation (Ktor-based)
         */
        fun default(): HttpClientFactory = KtorHttpClientFactory()
    }
}