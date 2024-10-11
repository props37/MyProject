package ru.livetyping.zarina.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import ru.livetyping.zarina.core.network.impl.ZarinaApiHeaderProvider
import ru.livetyping.zarina.core.network.impl.getZarinaUnauthorizedHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class NetworkModule {

    // TODO: [Top] Provide AUTHORIZED HttpClient

    @Provides
    @Singleton
    @ZarinaApiQualifier(ZarinaApi.UNAUTHORIZED)
    fun provideZarinaUnauthorizedHttpClient(): HttpClient {
        return getZarinaUnauthorizedHttpClient(
            json = Json, // TODO: [Top] Provide
            baseUrl = "", // TODO: [Top] Provide
            headerProvider = ZarinaApiHeaderProvider(),
        )
    }
}
