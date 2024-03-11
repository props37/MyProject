package ru.zarina.zarina.di.old

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.old.MindboxHeaderProvider
import ru.zarina.zarina.data.old.UserAgentHeaderProvider
import ru.zarina.zarina.data.old.auth.ZarinaAuth
import ru.zarina.zarina.data.old.auth.bearer
import ru.zarina.zarina.domain.AuthorizationToken
import ru.zarina.zarina.usecase.authorization.ClearDeviceAuthorizationTokenUseCase
import ru.zarina.zarina.usecase.authorization.GetAuthorizationTokenUseCase
import ru.zarina.zarina.util.base.usecase.invoke
import timber.log.Timber
import java.io.File

@Module
class NetworkModule {

    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    fun providesJson() = Json {
        isLenient = true
        ignoreUnknownKeys = true
        coerceInputValues = true
        explicitNulls = false
    }

    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    @Singleton
    fun providesTokenAuthorizationHttpClient(
        context: Context,
        getAuthorizationToken: GetAuthorizationTokenUseCase,
        clearDeviceAuthorizationToken: ClearDeviceAuthorizationTokenUseCase,
        json: Json,
        headerProvider: UserAgentHeaderProvider,
    ) = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(context, headerProvider)
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

    @Named(Qualifiers.Api.ZARINA)
    @Singleton
    fun providesHttpClient(
        context: Context,
        json: Json,
        headerProvider: UserAgentHeaderProvider,
    ) = HttpClient(OkHttp) {
        baseConfig(json)
        baseZarinaConfig(context, headerProvider)
    }

    @Named(Qualifiers.Api.MINDBOX_RESTRICTED)
    @Singleton
    fun providesMindboxSecretHttpClient(
        context: Context,
        json: Json,
        headerProvider: MindboxHeaderProvider,
    ) = HttpClient(OkHttp) {
        baseConfig(json)
        install(DefaultRequest) {
            url("https://api.mindbox.ru/v3/operations/sync/")
            headers {
                headerProvider.getHeaders().forEach { (key, value) ->
                    append(key, value)
                }
            }
        }
        install(HttpCache) {
            val cacheFile = File(context.cacheDir, CACHE_DIR_MINDBOX)
            privateStorage(FileStorage(cacheFile))
        }
    }

    @Named(Qualifiers.Api.ANYQUERY_AUTOCOMPLETE)
    @Singleton
    fun providesAnyQueryAutocompleteHttpClient(
        context: Context,
        json: Json,
    ) = HttpClient(OkHttp) {
        baseConfig(json)
        install(DefaultRequest) {
            url("https://autocomplete.diginetica.net/")
        }
        install(HttpCache) {
            val cacheFile = File(context.cacheDir, CACHE_DIR_MINDBOX)
            privateStorage(FileStorage(cacheFile))
        }
    }

    @Named(Qualifiers.Api.ANYQUERY_SEARCH)
    @Singleton
    fun providesAnyQuerySearchHttpClient(
        context: Context,
        json: Json,
    ) = HttpClient(OkHttp) {
        baseConfig(json)
        install(DefaultRequest) {
            url("https://sort.diginetica.net/")
        }
        install(HttpCache) {
            val cacheFile = File(context.cacheDir, CACHE_DIR_MINDBOX)
            privateStorage(FileStorage(cacheFile))
        }
    }

    private fun HttpClientConfig<*>.baseConfig(
        json: Json,
    ) {
        expectSuccess = true
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

    private fun HttpClientConfig<*>.baseZarinaConfig(
        context: Context,
        headerProvider: UserAgentHeaderProvider,
    ) {
        install(DefaultRequest) {
            url(BuildConfig.BACKEND_URL)
            headers {
                headerProvider.getHeaders().forEach { (key, value) ->
                    append(key, value)
                }
            }
        }
        install(HttpCache) {
            val cacheFile = File(context.cacheDir, CACHE_DIR_ZARINA)
            privateStorage(FileStorage(cacheFile))
        }
    }

    private fun AuthorizationToken.toBearerTokens(): BearerTokens {
        return BearerTokens(this.token, "")
    }

    companion object {
        private const val CACHE_DIR_ZARINA = "ktor-zarina-cache"
        private const val CACHE_DIR_MINDBOX = "ktor-mindbox-cache"
    }
}


