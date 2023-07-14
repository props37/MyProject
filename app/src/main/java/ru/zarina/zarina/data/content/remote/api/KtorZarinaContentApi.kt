package ru.zarina.zarina.data.content.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.content.remote.api.dto.BannerDto
import ru.zarina.zarina.data.content.remote.api.dto.SplashDto
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorZarinaContentApi(
    @Named(Qualifiers.Authorization.TOKEN)
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
}
