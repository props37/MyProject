package ru.livetyping.zarina.data.store.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.data.store.remote.api.dto.CountryStoresDto
import ru.livetyping.zarina.di.Qualifiers
import javax.inject.Inject

class StoreApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getStores(): List<CountryStoresDto> {
        return httpClient.get("/api/shops").body()
    }
}
