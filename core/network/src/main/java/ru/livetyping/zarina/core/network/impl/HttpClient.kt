package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber

internal fun getZarinaUnauthorizedHttpClient(
    json: Json,
    baseUrl: String,
    headerProvider: ZarinaApiHeaderProvider,
): HttpClient = HttpClient(OkHttp) {
    applyBaseConfig(json)
    applyZarinaConfig(baseUrl, headerProvider)
}

private fun HttpClientConfig<*>.applyBaseConfig(json: Json) {
    expectSuccess = true
    install(ContentNegotiation) {
        json(json)
    }
    // TODO: [Top] Do not install if there is no need in logging
    install(Logging) {
        level = LogLevel.ALL
        logger = object : Logger {
            override fun log(message: String) {
                Timber.tag(HTTP_CLIENT_TAG).v(message)
            }
        }
    }
    install(HttpTimeout) {
        // TODO: [Top] Increase timeouts for debug and qa builds
    }
}

private fun HttpClientConfig<*>.applyZarinaConfig(
    baseUrl: String,
    headerProvider: ZarinaApiHeaderProvider,
) {
    install(DefaultRequest) {
        url(baseUrl)
        headers {
            headerProvider.provide().forEach { (key, value) ->
                append(key, value)
            }
        }
    }
}

private const val HTTP_CLIENT_TAG = "HttpClient"
