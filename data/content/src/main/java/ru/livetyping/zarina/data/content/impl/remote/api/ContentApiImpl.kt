package ru.livetyping.zarina.data.content.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.data.content.impl.remote.api.dto.CatalogMenuByGenderDto
import javax.inject.Inject

internal class ContentApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : ContentApi {
    override suspend fun getCatalogMenu(): CatalogMenuByGenderDto {
        return httpClient.get("/api/menu").body()
    }
}
