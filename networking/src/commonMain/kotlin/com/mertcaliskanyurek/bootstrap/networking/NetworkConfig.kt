package com.mertcaliskanyurek.bootstrap.networking

/**
 * Configuration for network client
 */
data class NetworkConfig(
    val baseUrl: String,
    val authToken: String? = null,
    val enableLogging: Boolean = true,
    val requestTimeout: Long = 30_000L,
    val connectTimeout: Long = 30_000L,
    val socketTimeout: Long = 30_000L,
    val maxRetries: Int = 3
) {
    companion object {
        /**
         * Default development configuration
         * Note: Use 10.0.2.2 for Android emulator to reach host machine's localhost
         */
        fun development(
            baseUrl: String,
            authToken: String? = null
        ) = NetworkConfig(
            baseUrl = baseUrl,
            authToken = authToken,
            enableLogging = true
        )

        /**
         * Default production configuration
         */
        fun production(
            baseUrl: String,
            authToken: String? = null
        ) = NetworkConfig(
            baseUrl = baseUrl,
            authToken = authToken,
            enableLogging = false,
            requestTimeout = 15_000L,
            connectTimeout = 15_000L,
            socketTimeout = 15_000L
        )
    }
}