package com.mertcaliskanyurek.bootstrap.networking

import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

/**
 * Abstract HTTP client interface.
 *
 * Implementations should provide all HTTP methods (GET, POST, PUT, DELETE).
 * This abstraction hides the underlying HTTP client library (Ktor, OkHttp, etc.)
 *
 * Use the inline extension functions (get, post, put, delete) for ergonomic
 * reified type usage. The interface methods accept TypeInfo directly because
 * inline/reified cannot be used on interface (virtual) members.
 */
interface IHttpClient {

    suspend fun <T> get(
        url: String,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit = {}
    ): T

    suspend fun <T, R> post(
        url: String,
        body: T,
        bodyType: TypeInfo,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit = {}
    ): R

    suspend fun <T, R> put(
        url: String,
        body: T,
        bodyType: TypeInfo,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit = {}
    ): R

    suspend fun <T> delete(
        url: String,
        responseType: TypeInfo,
        configure: HttpRequestConfig.() -> Unit = {}
    ): T

    suspend fun close()
}

// ============================================================================
// Inline extension functions — provide ergonomic reified API for callers
// ============================================================================

suspend inline fun <reified T> IHttpClient.get(
    url: String,
    noinline configure: HttpRequestConfig.() -> Unit = {}
): T = get(url, typeInfo<T>(), configure)

suspend inline fun <reified T, reified R> IHttpClient.post(
    url: String,
    body: T,
    noinline configure: HttpRequestConfig.() -> Unit = {}
): R = post(url, body, typeInfo<T>(), typeInfo<R>(), configure)

suspend inline fun <reified T, reified R> IHttpClient.put(
    url: String,
    body: T,
    noinline configure: HttpRequestConfig.() -> Unit = {}
): R = put(url, body, typeInfo<T>(), typeInfo<R>(), configure)

suspend inline fun <reified T> IHttpClient.delete(
    url: String,
    noinline configure: HttpRequestConfig.() -> Unit = {}
): T = delete(url, typeInfo<T>(), configure)

/**
 * Configuration builder for HTTP requests
 */
class HttpRequestConfig {
    val headers: MutableMap<String, String> = mutableMapOf()

    fun header(key: String, value: String) {
        headers[key] = value
    }
}

/**
 * HTTP client configuration
 */
data class HttpClientConfig(
    val baseUrl: String = "",
    val connectTimeout: Long = 30000,
    val readTimeout: Long = 30000,
    val writeTimeout: Long = 30000,
    val logLevel: HttpLogLevel = HttpLogLevel.NONE,
    val retryCount: Int = 0
)

enum class HttpLogLevel {
    NONE, BASIC, HEADERS, BODY, ALL
}
