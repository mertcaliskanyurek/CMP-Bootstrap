package com.mertcaliskanyurek.bootstrap.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ApiClient(val httpClient: HttpClient) {

    suspend inline fun <reified T> get(
        url: String,
        noinline configure: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return httpClient.get(url) {
            configure()
        }.body()
    }

    suspend inline fun <reified T, reified R> post(
        url: String,
        body: T,
        noinline configure: HttpRequestBuilder.() -> Unit = {}
    ): R {
        return httpClient.post(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
            configure()
        }.body()
    }

    suspend inline fun <reified T, reified R> put(
        url: String,
        body: T,
        noinline configure: HttpRequestBuilder.() -> Unit = {}
    ): R {
        return httpClient.put(url) {
            contentType(ContentType.Application.Json)
            setBody(body)
            configure()
        }.body()
    }

    suspend inline fun <reified T> delete(
        url: String,
        noinline configure: HttpRequestBuilder.() -> Unit = {}
    ): T {
        return httpClient.delete(url) {
            configure()
        }.body()
    }
    
}
