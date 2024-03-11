package ru.zarina.zarina.data.old.device.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.old.device.remote.api.dto.TokenDto
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorZarinaDeviceApi(
    @Named(Qualifiers.Api.ZARINA)
    private val client: HttpClient,
) : IZarinaDeviceApi {

    override suspend fun getToken(): TokenDto {
        val response = client.get("/api/device")
        return response.body()
    }

}
