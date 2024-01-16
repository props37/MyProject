package ru.zarina.zarina.di.rework

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.rework.common.remote.headerprovider.ZarinaApiHeaderProvider
import ru.zarina.zarina.data.rework.common.remote.ktor.plugin.ZarinaAuth
import ru.zarina.zarina.data.rework.common.remote.ktor.plugin.bearer
import ru.zarina.zarina.domain.rework.authorization.AuthorizationTokens
import ru.zarina.zarina.usecase.rework.authorization.GetAuthorizationTokensUseCase
import ru.zarina.zarina.usecase.rework.authorization.RefreshAuthorizationTokensUseCase
import ru.zarina.zarina.utils.clean.invoke
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    fun provideAuthorizedZarinaHttpClient(
        json: Json,
        zarinaApiHeaderProvider: ZarinaApiHeaderProvider,
        getAuthorizationTokens: GetAuthorizationTokensUseCase,
        refreshAuthorizationTokens: RefreshAuthorizationTokensUseCase,
    ): HttpClient = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(zarinaApiHeaderProvider)
        install(ZarinaAuth) {
            bearer {
                loadTokens {
                    getAuthorizationTokens().getOrNull()?.toBearerTokens()
                }

                refreshTokens {
                    refreshAuthorizationTokens()
                    getAuthorizationTokens().getOrNull()?.toBearerTokens()
                }
            }
        }
    }

    @Provides
    @Singleton
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.UNAUTHORIZED)
    fun provideUnauthorizedZarinaHttpClient(
        json: Json,
        zarinaApiHeaderProvider: ZarinaApiHeaderProvider,
    ): HttpClient = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(zarinaApiHeaderProvider)
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
        install(HttpTimeout)
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

    private fun AuthorizationTokens.toBearerTokens(): BearerTokens {
        return BearerTokens(accessToken.value, refreshToken.value)
    }

    companion object {
        private const val HTTP_CLIENT_TAG = "HttpClient"
    }
}
