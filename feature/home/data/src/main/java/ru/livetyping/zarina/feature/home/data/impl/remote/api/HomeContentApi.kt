package ru.livetyping.zarina.feature.home.data.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiQualifier
import ru.livetyping.zarina.feature.home.data.impl.remote.api.dto.HomeContentDto
import javax.inject.Inject

internal class HomeContentApi @Inject constructor(
    @ZarinaApiQualifier(ZarinaApi.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getHomeContent(): HomeContentDto {
        return httpClient.get("/api/v1/main/banners").body()
    }
}
