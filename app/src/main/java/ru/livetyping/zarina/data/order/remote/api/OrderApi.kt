package ru.livetyping.zarina.data.order.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.data.order.remote.api.dto.GetOrdersDto
import ru.livetyping.zarina.di.Qualifiers
import javax.inject.Inject

class OrderApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getOrders(page: Int): GetOrdersDto {
        return httpClient.get("/api/v1/orders") {
            parameter("page", page)
        }.body()
    }
}
