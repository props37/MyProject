package ru.livetyping.zarina.core.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.buildutil.BuildType
import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl
import ru.livetyping.zarina.core.network.auth.BearerTokenService
import ru.livetyping.zarina.core.network.auth.ZarinaHttpClientBearerTokenCleaner
import ru.livetyping.zarina.core.network.impl.ZarinaApiHeaderProvider
import ru.livetyping.zarina.core.network.impl.ZarinaHttpClientBearerTokenCleanerImpl
import ru.livetyping.zarina.core.network.impl.getAnyQueryAutocompleteHttpClient
import ru.livetyping.zarina.core.network.impl.getZarinaAuthorizedHttpClient
import ru.livetyping.zarina.core.network.impl.getZarinaUnauthorizedHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkModule {

    @Binds
    abstract fun bindZarinaHttpClientBearerTokenCleaner(
        impl: ZarinaHttpClientBearerTokenCleanerImpl,
    ): ZarinaHttpClientBearerTokenCleaner

    companion object {
        @Provides
        @Singleton
        @ZarinaApi(ZarinaApiType.AUTHORIZED)
        fun provideZarinaAuthorizedHttpClient(
            @ZarinaBaseUrl
            baseUrl: String,
            bearerTokenService: BearerTokenService,
            buildType: BuildType,
            @NetworkJson
            json: Json,
        ): HttpClient {
            return getZarinaAuthorizedHttpClient(
                json = json,
                baseUrl = baseUrl,
                headerProvider = ZarinaApiHeaderProvider(),
                bearerTokenService = bearerTokenService,
                buildType = buildType,
            )
        }

        @Provides
        @Singleton
        @ZarinaApi(ZarinaApiType.UNAUTHORIZED)
        fun provideZarinaUnauthorizedHttpClient(
            @ZarinaBaseUrl
            baseUrl: String,
            buildType: BuildType,
            @NetworkJson
            json: Json,
        ): HttpClient {
            return getZarinaUnauthorizedHttpClient(
                json = json,
                baseUrl = baseUrl,
                headerProvider = ZarinaApiHeaderProvider(),
                buildType = buildType,
            )
        }

        @Provides
        @Singleton
        @AnyQueryAutocompleteApi
        fun provideAnyQueryAutocompleteHttpClient(
            @NetworkJson
            json: Json,
            buildType: BuildType,
        ): HttpClient {
            return getAnyQueryAutocompleteHttpClient(
                json = json,
                buildType = buildType,
            )
        }

        @Provides
        @Singleton
        @NetworkJson
        fun provideJson(): Json {
            return Json {
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
                explicitNulls = false
            }
        }
    }
}
