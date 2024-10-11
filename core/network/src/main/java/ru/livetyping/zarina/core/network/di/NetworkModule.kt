package ru.livetyping.zarina.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.network.auth.BearerTokenLoader
import ru.livetyping.zarina.core.network.auth.BearerTokenRefresher
import ru.livetyping.zarina.core.network.impl.ZarinaApiHeaderProvider
import ru.livetyping.zarina.core.network.impl.getZarinaAuthorizedHttpClient
import ru.livetyping.zarina.core.network.impl.getZarinaUnauthorizedHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    private val json by lazy {
        Json {
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
        }
    }

    @Provides
    @Singleton
    @ZarinaApiQualifier(ZarinaApi.AUTHORIZED)
    fun provideZarinaUnauthorizedHttpClient(
        bearerTokenLoader: BearerTokenLoader,
        bearerTokenRefresher: BearerTokenRefresher,
    ): HttpClient {
        return getZarinaAuthorizedHttpClient(
            json = json,
            baseUrl = "", // TODO: [Top] Provide
            headerProvider = ZarinaApiHeaderProvider(),
            bearerTokenLoader = bearerTokenLoader,
            bearerTokenRefresher = bearerTokenRefresher,
        )
    }

    @Provides
    @Singleton
    @ZarinaApiQualifier(ZarinaApi.UNAUTHORIZED)
    fun provideZarinaUnauthorizedHttpClient(): HttpClient {
        return getZarinaUnauthorizedHttpClient(
            json = json,
            baseUrl = "", // TODO: [Top] Provide
            headerProvider = ZarinaApiHeaderProvider(),
        )
    }
}
