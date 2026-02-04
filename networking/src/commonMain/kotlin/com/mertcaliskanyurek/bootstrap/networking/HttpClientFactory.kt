package com.mertcaliskanyurek.bootstrap.networking

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Factory for creating configured HttpClient instances
 */
object HttpClientFactory {

    /**
     * Creates a configured HttpClient with all necessary plugins
     */
    fun create(config: NetworkConfig): HttpClient {
        return HttpClient {
            // Base URL configuration
            defaultRequest {
                url.takeFrom(config.baseUrl)
                contentType(ContentType.Application.Json)

                // Add auth token if available
                config.authToken?.let { token ->
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }

            // JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    classDiscriminator = "type"
                    // Allow polymorphic serialization with custom class discriminators
                    useArrayPolymorphism = false
                })
            }

            // Logging
            install(Logging) {
                logger = Logger.DEFAULT
                level = if (config.enableLogging) LogLevel.ALL else LogLevel.NONE
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }

            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = config.requestTimeout
                connectTimeoutMillis = config.connectTimeout
                socketTimeoutMillis = config.socketTimeout
            }

            // Retry configuration
            install(HttpRequestRetry) {
                retryOnServerErrors(maxRetries = config.maxRetries)
                exponentialDelay()
            }
        }
    }
}