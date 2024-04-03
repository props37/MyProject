package ru.livetyping.zarina.data.old.content.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.data.old.content.remote.api.dto.BannerDto
import ru.livetyping.zarina.data.old.content.remote.api.dto.SelectionDto
import ru.livetyping.zarina.data.old.content.remote.api.dto.SplashDto
import ru.livetyping.zarina.di.old.Qualifiers

@Factory
class KtorZarinaContentApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaContentApi {
    override suspend fun getOnboardingSplash(): SplashDto {
        val response = client.get("/api/main/splash")
        return response.body()
    }

    override suspend fun getBanners(): List<BannerDto> {
        val response = client.get("/api/main/banners")
        return response.body()
    }

    override suspend fun getSelections(): List<SelectionDto> {
        val response = client.get("/api/main/products/groups")
        return response.body()
    }
}
