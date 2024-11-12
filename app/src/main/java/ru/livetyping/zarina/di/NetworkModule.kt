package ru.livetyping.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.headers
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.data.common.remote.api.zarina.ZarinaApiHeaderProvider
import ru.livetyping.zarina.domain.authorization.AuthorizationTokens
import ru.livetyping.zarina.usecase.authorization.FetchUnauthorizedUserAuthorizationTokensUseCase
import ru.livetyping.zarina.usecase.authorization.GetAuthorizationTokensFlowUseCase
import ru.livetyping.zarina.usecase.authorization.RefreshAuthorizationTokensUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.ktor.clearBearerTokens
import timber.log.Timber
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    fun provideAuthorizedZarinaHttpClient(
        json: Json,
        zarinaApiHeaderProvider: ZarinaApiHeaderProvider,
        getAuthorizationTokensFlow: GetAuthorizationTokensFlowUseCase,
        fetchUnauthorizedUserAuthorizationTokens: FetchUnauthorizedUserAuthorizationTokensUseCase,
        refreshAuthorizationTokens: RefreshAuthorizationTokensUseCase,
    ): HttpClient = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(zarinaApiHeaderProvider)
        install(Auth) {
            bearer {
                loadTokens {
                    var tokens = getAuthorizationTokensFlow().firstOrNull()?.getOrNull()
                    Timber.tag(HTTP_CLIENT_TAG).v("Authorization tokens loaded: $tokens")
                    if (tokens == null) {
                        Timber.tag(HTTP_CLIENT_TAG).v("Loaded authorization tokens are null, trying to fetch")
                        tokens = fetchUnauthorizedUserAuthorizationTokens().getOrNull()
                        Timber.tag(HTTP_CLIENT_TAG).v("Fetched authorization tokens: $tokens")
                    }
                    tokens?.toBearerTokens()
                }

                refreshTokens {
                    val tokens = refreshAuthorizationTokens().getOrNull()
                    Timber.tag(HTTP_CLIENT_TAG).v("Authorization tokens refreshed: $tokens")
                    tokens?.toBearerTokens()
                }
            }
        }
    }.also { client ->
        client.loadTokensIfNotPresent()
    }

    @Provides
    @Singleton
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.UNAUTHORIZED)
    fun provideUnauthorizedZarinaHttpClient(
        json: Json,
        zarinaApiHeaderProvider: ZarinaApiHeaderProvider,
    ): HttpClient = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(zarinaApiHeaderProvider)
    }

    @Provides
    @Singleton
    @Qualifiers.AnyQuery(Qualifiers.AnyQueryType.AUTOCOMPLETE)
    fun provideAnyQueryAutocompleteHttpClient(
        json: Json,
    ): HttpClient = HttpClient(OkHttp) {
        baseConfig(json)
        install(DefaultRequest) {
            url(ANY_QUERY_BASE_URL_AUTOCOMPLETE)
        }
    }

    private fun HttpClientConfig<*>.baseConfig(json: Json) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.tag(HTTP_CLIENT_TAG).v(message)
                }
            }
        }
        install(HttpTimeout) {
            val isDebug = BuildConfig.BUILD_TYPE == "debug"
            val isQa = BuildConfig.BUILD_TYPE == "qa"
            if (isDebug || isQa) {
                val timeoutMillis = 60.seconds.inWholeMilliseconds
                requestTimeoutMillis = timeoutMillis
                socketTimeoutMillis = timeoutMillis
                connectTimeoutMillis = timeoutMillis
            }
        }
    }

    private fun HttpClientConfig<*>.baseZarinaConfig(
        zarinaApiHeaderProvider: ZarinaApiHeaderProvider,
    ) {
        install(DefaultRequest) {
            url(BuildConfig.BACKEND_URL)
            headers {
                zarinaApiHeaderProvider.provide().forEach { (key, value) ->
                    append(key, value)
                }
            }
        }
    }

    private fun HttpClient.loadTokensIfNotPresent() {
        this.plugin(HttpSend).intercept { request ->
            val call = execute(request)
            val isAccessTokenPresent = request.headers[HEADER_AUTHORIZATION] != null
            val response = call.response
            val wasAccessTokenRequired = response.status == HttpStatusCode.Forbidden
                    || response.status == HttpStatusCode.Unauthorized
            if (wasAccessTokenRequired && !isAccessTokenPresent) {
                Timber
                    .tag(HTTP_CLIENT_TAG)
                    .w("${response.status} received and access token is not present. Initiating loading of tokens")
                this@loadTokensIfNotPresent.clearBearerTokens()
            }
            call
        }
    }

    private fun AuthorizationTokens.toBearerTokens(): BearerTokens {
        return BearerTokens(accessToken.value, refreshToken.value)
    }

    companion object {
        private const val ANY_QUERY_BASE_URL_AUTOCOMPLETE = "https://autocomplete.diginetica.net/"

        private const val HEADER_AUTHORIZATION = "Authorization"

        private const val HTTP_CLIENT_TAG = "HttpClient"
    }
}
