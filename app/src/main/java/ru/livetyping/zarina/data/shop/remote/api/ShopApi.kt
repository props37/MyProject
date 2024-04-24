package ru.livetyping.zarina.data.shop.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.data.shop.remote.api.dto.CountryShopsDto
import ru.livetyping.zarina.di.Qualifiers
import javax.inject.Inject

class ShopApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getShops(): List<CountryShopsDto> {
        return httpClient.get("/api/shops").body()
    }
}
