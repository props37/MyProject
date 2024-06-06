package ru.livetyping.zarina.data.content.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.BuildConfig
import ru.livetyping.zarina.data.content.remote.api.dto.HomeBannersDto
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.common.Url
import javax.inject.Inject

class ContentApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    fun getOnboardingBannerUrl(): Url {
        return Url("${BuildConfig.BACKEND_URL}/api/v1/main/splash/")
    }

    suspend fun getHomeBanners(): HomeBannersDto {
        return httpClient.get("/api/v1/main/banners").body()
    }
}
