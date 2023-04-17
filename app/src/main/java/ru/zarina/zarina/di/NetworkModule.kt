package ru.zarina.zarina.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ru.zarina.zarina.BuildConfig
import timber.log.Timber
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

    @Singleton
    @Provides
    fun providesHttpClient(
        json: Json,
    ) = HttpClient(CIO) {
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
