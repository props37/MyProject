package ru.zarina.zarina.data.content.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.content.remote.api.dto.SplashDto
import javax.inject.Inject

class ContentApi @Inject constructor(
    private val client: HttpClient,
) : IContentApi {
    override suspend fun getOnboardingSplash(): SplashDto {
        val response = client.get("/api/main/splash")
        return response.body()
    }

}
