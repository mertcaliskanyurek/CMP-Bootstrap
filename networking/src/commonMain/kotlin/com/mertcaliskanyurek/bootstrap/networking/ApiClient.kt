package com.mertcaliskanyurek.bootstrap.networking

/**
 * High-level API client wrapper.
 *
 * Projects should use this instead of IHttpClient directly.
 * It doesn't expose any Ktor implementation details.
 */
class ApiClient(val httpClient: IHttpClient) {

    suspend inline fun <reified T> get(
        url: String,
        noinline configure: HttpRequestConfig.() -> Unit = {}
    ): T = mapNetworkErrors {
        httpClient.get(url, configure)
    }

    suspend inline fun <reified T, reified R> post(
        url: String,
        body: T,
        noinline configure: HttpRequestConfig.() -> Unit = {}
    ): R = mapNetworkErrors {
        httpClient.post(url, body, configure)
    }

    suspend inline fun <reified T, reified R> put(
        url: String,
        body: T,
        noinline configure: HttpRequestConfig.() -> Unit = {}
    ): R = mapNetworkErrors {
        httpClient.put(url, body, configure)
    }

    suspend inline fun <reified T> delete(
        url: String,
        noinline configure: HttpRequestConfig.() -> Unit = {}
    ): T = mapNetworkErrors {
        httpClient.delete(url, configure)
    }

    suspend fun close() {
        httpClient.close()
    }
    
    companion object {
        /**
         * Create an ApiClient with default configuration
         */
        fun create(config: HttpClientConfig = HttpClientConfig()): ApiClient {
            val factory = HttpClientFactory.default()
            val httpClient = factory.create(config)
            return ApiClient(httpClient)
        }
    }
}
