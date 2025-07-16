package ru.livetyping.zarina.core.network.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.analytics.HttpErrorLogger
import ru.livetyping.zarina.core.buildutil.AppVersionCode
import ru.livetyping.zarina.core.buildutil.BuildType
import ru.livetyping.zarina.core.buildutil.MindboxKey
import ru.livetyping.zarina.core.buildutil.ZarinaBaseUrl
import ru.livetyping.zarina.core.network.auth.BearerTokenService
import ru.livetyping.zarina.core.network.auth.ZarinaHttpClientBearerTokenCleaner
import ru.livetyping.zarina.core.network.auth.ZarinaHttpClientBearerTokenCleanerImpl
import ru.livetyping.zarina.core.network.client.getAnyQueryAutocompleteHttpClient
import ru.livetyping.zarina.core.network.client.getMindboxHttpClient
import ru.livetyping.zarina.core.network.client.getZarinaAuthorizedHttpClient
import ru.livetyping.zarina.core.network.client.getZarinaUnauthorizedHttpClient
import ru.livetyping.zarina.core.network.zarina.ZarinaApiHeaderProvider
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
            @AppVersionCode
            appVersionCode: Int,
            errorLogger: HttpErrorLogger,
        ): HttpClient {
            return getZarinaAuthorizedHttpClient(
                json = json,
                baseUrl = baseUrl,
                headerProvider = ZarinaApiHeaderProvider(appVersionCode),
                bearerTokenService = bearerTokenService,
                buildType = buildType,
                errorLogger = errorLogger,
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
            @AppVersionCode
            appVersionCode: Int,
            errorLogger: HttpErrorLogger,
        ): HttpClient {
            return getZarinaUnauthorizedHttpClient(
                json = json,
                baseUrl = baseUrl,
                headerProvider = ZarinaApiHeaderProvider(appVersionCode),
                buildType = buildType,
                errorLogger = errorLogger,
            )
        }

        @Provides
        @Singleton
        @AnyQueryAutocompleteApi
        fun provideAnyQueryAutocompleteHttpClient(
            @NetworkJson
            json: Json,
            buildType: BuildType,
            errorLogger: HttpErrorLogger,
        ): HttpClient {
            return getAnyQueryAutocompleteHttpClient(
                json = json,
                buildType = buildType,
                errorLogger = errorLogger,
            )
        }

        @Provides
        @Singleton
        @MindboxApi
        fun provideMindboxHttpClient(
            @NetworkJson
            json: Json,
            buildType: BuildType,
            @MindboxKey
            mindboxKey: String,
            errorLogger: HttpErrorLogger,
        ): HttpClient {
            return getMindboxHttpClient(
                json = json,
                buildType = buildType,
                mindboxKey = mindboxKey,
                errorLogger = errorLogger,
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
