package ru.zarina.zarina.di.reworked

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.common.remote.headerprovider.ZarinaApiHeaderProvider
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
    ): HttpClient = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(zarinaApiHeaderProvider)
        // TODO: [High] Add authorization
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

    companion object {
        private const val HTTP_CLIENT_TAG = "HttpClient"
    }
}
