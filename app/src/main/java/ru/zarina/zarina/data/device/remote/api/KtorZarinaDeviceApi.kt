package ru.zarina.zarina.data.device.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.device.remote.api.dto.TokenDto
import javax.inject.Inject

class KtorZarinaDeviceApi @Inject constructor(
    private val client: HttpClient,
) : IZarinaDeviceApi {

    override suspend fun getToken(): TokenDto {
        val response = client.get("/api/device")
        return response.body()
    }

}
