package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.buildutil.BuildType
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

internal fun HttpClientConfig<*>.applyBaseConfig(
    json: Json,
    buildType: BuildType,
) {
    expectSuccess = true
    install(ContentNegotiation) {
        json(json)
    }

    val isDebugBuild = buildType == BuildType.DEBUG
    val isQaBuild = buildType == BuildType.QA

    if (isDebugBuild || isQaBuild) {
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag(HTTP_CLIENT_TAG).v(message)
                }
            }
        }
    }

    install(HttpTimeout) {
        if (isDebugBuild || isQaBuild) {
            val timeoutMillis = 20.seconds.inWholeMilliseconds
            requestTimeoutMillis = timeoutMillis
            socketTimeoutMillis = timeoutMillis
            connectTimeoutMillis = timeoutMillis
        }
    }
}

internal const val HTTP_CLIENT_TAG = "HttpClient"
