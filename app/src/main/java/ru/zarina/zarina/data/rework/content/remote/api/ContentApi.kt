package ru.zarina.zarina.data.rework.content.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.rework.content.remote.api.dto.HomeBannersDto
import ru.zarina.zarina.di.rework.Qualifiers
import ru.zarina.zarina.domain.rework.common.Url
import javax.inject.Inject

class ContentApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    fun getOnboardingBannerUrl(): Url {
        return Url("${BuildConfig.BACKEND_URL}/api/v1/main/splash/")
    }

    suspend fun getHomeBanners(): HomeBannersDto {
        return httpClient.get("/api/v1/main/banners").body()
    }
}
