package ru.livetyping.zarina.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl
import ru.livetyping.zarina.core.network.auth.BearerTokenService
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
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    fun provideZarinaUnauthorizedHttpClient(
        @ZarinaBaseUrl
        baseUrl: String,
        bearerTokenService: BearerTokenService,
    ): HttpClient {
        return getZarinaAuthorizedHttpClient(
            json = json,
            baseUrl = baseUrl,
            headerProvider = ZarinaApiHeaderProvider(),
            bearerTokenService = bearerTokenService,
        )
    }

    @Provides
    @Singleton
    @ZarinaApi(ZarinaApiType.UNAUTHORIZED)
    fun provideZarinaUnauthorizedHttpClient(
        @ZarinaBaseUrl
        baseUrl: String,
    ): HttpClient {
        return getZarinaUnauthorizedHttpClient(
            json = json,
            baseUrl = baseUrl,
            headerProvider = ZarinaApiHeaderProvider(),
        )
    }
}
