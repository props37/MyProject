package ru.livetyping.zarina.feature.home.data.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.feature.home.data.impl.remote.api.dto.HomeContentDto
import javax.inject.Inject

internal class HomeContentApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : HomeContentApi {
    override suspend fun getHomeContent(): HomeContentDto {
        return httpClient.get("/api/v1/main/banners").body()
    }
}
