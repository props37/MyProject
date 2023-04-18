package ru.zarina.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.UserAgentHeaderProvider
import ru.zarina.zarina.data.ktor.plugins.auth.ZarinaAuth
import ru.zarina.zarina.data.ktor.plugins.auth.bearer
import ru.zarina.zarina.domain.AuthorizationToken
import ru.zarina.zarina.usecase.authorization.ClearDeviceAuthorizationTokenUseCase
import ru.zarina.zarina.usecase.authorization.GetAuthorizationTokenUseCase
import ru.zarina.zarina.utils.clean.invoke
import timber.log.Timber
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesJson() = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    @Authorization(Authorization.Type.NONE)
    @Singleton
    @Provides
    fun providesHttpClient(
        json: Json,
        headerProvider: UserAgentHeaderProvider,
    ) = HttpClient(CIO) {
        baseConfig(json, headerProvider)
    }

    @Authorization(Authorization.Type.TOKEN)
    @Singleton
    @Provides
    fun providesTokenAuthorizationHttpClient(
        getAuthorizationToken: GetAuthorizationTokenUseCase,
        clearDeviceAuthorizationToken: ClearDeviceAuthorizationTokenUseCase,
        json: Json,
        headerProvider: UserAgentHeaderProvider,
    ) = HttpClient(CIO) {
        baseConfig(json, headerProvider)
        install(ZarinaAuth) {
            bearer {
                loadTokens {
                    getAuthorizationToken().getOrNull()?.toBearerTokens()
                }
                refreshTokens {
                    clearDeviceAuthorizationToken()
                    getAuthorizationToken().getOrNull()?.toBearerTokens()
                }
            }
        }
    }

    private fun HttpClientConfig<CIOEngineConfig>.baseConfig(
        json: Json,
        headerProvider: UserAgentHeaderProvider,
    ) {
        expectSuccess = true
        install(DefaultRequest) {
            url(BuildConfig.BACKEND_URL)
            headers {
                headerProvider.getHeaders().forEach { (key, value) ->
                    append(key, value)
                }
            }
        }
        install(ContentNegotiation) {
            json(json)
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) = Timber.tag("ktor").v(message)
            }
        }
    }

    private fun AuthorizationToken.toBearerTokens(): BearerTokens {
        return BearerTokens(this.token, "")
    }
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorization(@Suppress("unused") val type: Type) {
    enum class Type { NONE, TOKEN }
}
