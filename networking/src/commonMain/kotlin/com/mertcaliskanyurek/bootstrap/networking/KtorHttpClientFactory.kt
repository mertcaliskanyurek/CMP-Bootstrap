package com.mertcaliskanyurek.bootstrap.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.TypeInfo
import kotlinx.serialization.json.Json

/**
 * Ktor-based implementation of HttpClientFactory.
 * This is an internal implementation detail - use HttpClientFactory.default() or HttpClientFactory interface instead.
 */
internal class KtorHttpClientFactory : HttpClientFactory {

    override fun create(config: HttpClientConfig): IHttpClient {
        return KtorHttpClientImpl(createKtorClient(config))
    }

    private fun createKtorClient(config: HttpClientConfig): HttpClient {
        return HttpClient {
            // Base URL configuration
            defaultRequest {
                if (config.baseUrl.isNotEmpty()) {
                    url.takeFrom(config.baseUrl)
                }
                contentType(ContentType.Application.Json)
            }

            // JSON serialization
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                    classDiscriminator = "type"
                    useArrayPolymorphism = false
                })
            }

            // Logging
            install(Logging) {
                logger = Logger.DEFAULT
                level = when (config.logLevel) {
                    HttpLogLevel.NONE -> LogLevel.NONE
                    HttpLogLevel.BASIC -> LogLevel.INFO
                    HttpLogLevel.HEADERS -> LogLevel.HEADERS
                    HttpLogLevel.BODY -> LogLevel.BODY
                    HttpLogLevel.ALL -> LogLevel.ALL
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }

            // Timeout configuration
            install(HttpTimeout) {
                requestTimeoutMillis = config.readTimeout
                connectTimeoutMillis = config.connectTimeout
                socketTimeoutMillis = config.writeTimeout
            }

            // Retry configuration
            if (config.retryCount > 0) {
                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = config.retryCount)
                    exponentialDelay()
                }
            }

            // Throw ResponseException on non-2xx responses
            expectSuccess = true
        }
    }
}

/**
 * Internal Ktor-based implementation of the IHttpClient interface
 */
internal class KtorHttpClientImpl(private val ktorClient: HttpClient) : IHttpClient {

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> get(
        url: String,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit
    ): T {
        val requestConfig = HttpRequestConfig().apply(configure)
        return ktorClient.get(url) {
            applyConfig(requestConfig)
        }.body(responseType) as T
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T, R> post(
        url: String,
        body: T,
        bodyType: TypeInfo,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit
    ): R {
        val requestConfig = HttpRequestConfig().apply(configure)
        return ktorClient.post(url) {
            applyConfig(requestConfig)
            contentType(ContentType.Application.Json)
            setBody(body, bodyType)
        }.body(responseType) as R
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T, R> put(
        url: String,
        body: T,
        bodyType: TypeInfo,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit
    ): R {
        val requestConfig = HttpRequestConfig().apply(configure)
        return ktorClient.put(url) {
            applyConfig(requestConfig)
            contentType(ContentType.Application.Json)
            setBody(body, bodyType)
        }.body(responseType) as R
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> delete(
        url: String,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit
    ): T {
        val requestConfig = HttpRequestConfig().apply(configure)
        return ktorClient.delete(url) {
            applyConfig(requestConfig)
        }.body(responseType) as T
    }

    override suspend fun close() {
        ktorClient.close()
    }

    private fun io.ktor.client.request.HttpRequestBuilder.applyConfig(config: HttpRequestConfig) {
        config.headers.forEach { (key, value) ->
            header(key, value)
        }
    }
}
