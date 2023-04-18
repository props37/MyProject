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
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.zarina.zarina.BuildConfig
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
    ) = HttpClient(CIO) {
        baseConfig(json)
    }

    @Authorization(Authorization.Type.TOKEN)
    @Singleton
    @Provides
    fun providesTokenAuthorizationHttpClient(
        json: Json,
    ) = HttpClient(CIO) {
        baseConfig(json)
        install(Auth) {
            // TODO
        }
    }

    private fun HttpClientConfig<CIOEngineConfig>.baseConfig(json: Json) {
        expectSuccess = true
        install(DefaultRequest) {
            url(BuildConfig.BACKEND_URL)
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
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Authorization(@Suppress("unused") val type: Type) {
    enum class Type { NONE, TOKEN }
}
