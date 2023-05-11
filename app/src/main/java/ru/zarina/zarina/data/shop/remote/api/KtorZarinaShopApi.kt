package ru.zarina.zarina.data.shop.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.shop.remote.api.dto.ShopsResponseDto
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaShopApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaShopApi {

    override suspend fun getShops(): ShopsResponseDto {
        val response = client.get("/api/shops")
        return response.body()
    }

}

