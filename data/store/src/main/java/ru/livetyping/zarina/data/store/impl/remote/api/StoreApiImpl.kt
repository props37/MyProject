package ru.livetyping.zarina.data.store.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.data.store.impl.remote.api.dto.StoresDto
import javax.inject.Inject

internal class StoreApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : StoreApi {
    override suspend fun getStores(): List<StoresDto> {
        return httpClient.get("/api/shops").body()
    }
}
