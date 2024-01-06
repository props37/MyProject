package ru.zarina.zarina.data.rework.home.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.rework.home.remote.api.dto.HomeBannersDto
import ru.zarina.zarina.di.rework.Qualifiers
import javax.inject.Inject

class HomeApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getBanners(): HomeBannersDto {
        return httpClient.get("/api/v1/main/banners").body()
    }
}
