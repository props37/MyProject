package ru.livetyping.zarina.core.network.impl

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.plugin
import io.ktor.client.request.headers
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.buildutil.BuildType
import ru.livetyping.zarina.core.network.auth.BearerTokenService
import ru.livetyping.zarina.core.network.auth.BearerTokens
import ru.livetyping.zarina.core.network.util.clearBearerTokens
import timber.log.Timber

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
    installAuthPlugin(bearerTokenService)
}.also { client ->
    client.loadBearerTokensOnAuthorizationFailure()
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

private fun HttpClientConfig<*>.installAuthPlugin(bearerTokenService: BearerTokenService) {
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
}

private fun HttpClient.loadBearerTokensOnAuthorizationFailure() {
    this.plugin(HttpSend).intercept { request ->
        val isAccessTokenPresent = request.headers[HEADER_AUTHORIZATION] != null
        val call = execute(request)
        val response = call.response

        val status = response.status
        val requestAuthorizationFailed =
            status == HttpStatusCode.Forbidden
                    || status == HttpStatusCode.Unauthorized
        if (!isAccessTokenPresent && requestAuthorizationFailed) {
            Timber
                .tag(HTTP_CLIENT_TAG)
                .w("${response.status} received and access token is not present, reload tokens")
            this@loadBearerTokensOnAuthorizationFailure.clearBearerTokens()
        }
        call
    }
}

private const val HEADER_AUTHORIZATION = "Authorization"
