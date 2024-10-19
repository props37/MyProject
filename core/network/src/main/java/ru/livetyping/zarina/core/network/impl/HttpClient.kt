package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.plugins.pluginOrNull
import io.ktor.client.request.headers
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.buildutil.BuildType
import ru.livetyping.zarina.core.network.auth.BearerTokenService
import ru.livetyping.zarina.core.network.auth.BearerTokens
import timber.log.Timber
import kotlin.time.Duration.Companion.seconds

internal fun getZarinaUnauthorizedHttpClient(
    json: Json,
    baseUrl: String,
    headerProvider: ZarinaApiHeaderProvider,
    buildType: BuildType,
): HttpClient = HttpClient(OkHttp) {
    applyBaseConfig(json, buildType)
    applyZarinaConfig(baseUrl, headerProvider)
}

internal fun getZarinaAuthorizedHttpClient(
    json: Json,
    baseUrl: String,
    headerProvider: ZarinaApiHeaderProvider,
    bearerTokenService: BearerTokenService,
    buildType: BuildType,
): HttpClient = HttpClient(OkHttp) {
    applyBaseConfig(json, buildType)
    applyZarinaConfig(baseUrl, headerProvider)
    install(Auth) {
        bearer {
            loadTokens {
                val tokens = bearerTokenService.loadTokens()
                Timber.tag(HTTP_CLIENT_TAG).v("Bearer tokens loaded: $tokens")
                tokens?.toKtorBearerTokens()
            }

            refreshTokens {
                val oldTokens = this.oldTokens?.let { BearerTokens.from(it) }
                val newTokens = bearerTokenService.refreshTokens(oldTokens)
                Timber.tag(HTTP_CLIENT_TAG).v("Bearer tokens refreshed: $newTokens")
                newTokens?.toKtorBearerTokens()
            }
        }
    }
}.also { client ->
    client.loadBearerTokensOnAuthorizationFailure()
}

private fun HttpClientConfig<*>.applyBaseConfig(
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

private fun HttpClient.loadBearerTokensOnAuthorizationFailure() {
    this.plugin(HttpSend).intercept { request ->
        val isAccessTokenPresent = request.headers[HEADER_AUTHORIZATION] != null
        val call = execute(request)
        val response = call.response

        val status = response.status
        val requestAuthorizationFailed =
            status == HttpStatusCode.Forbidden
                    && status == HttpStatusCode.Unauthorized
        if (!isAccessTokenPresent && requestAuthorizationFailed) {
            Timber
                .tag(HTTP_CLIENT_TAG)
                .w("${response.status} received and access token is not present, reload tokens")
            this@loadBearerTokensOnAuthorizationFailure.clearBearerTokens()
        }
        call
    }
}

private fun HttpClient.clearBearerTokens() {
    this.pluginOrNull(Auth)
        ?.providers
        ?.filterIsInstance<BearerAuthProvider>()
        ?.firstOrNull()
        ?.clearToken()
}

private const val HEADER_AUTHORIZATION = "Authorization"

private const val HTTP_CLIENT_TAG = "HttpClient"
