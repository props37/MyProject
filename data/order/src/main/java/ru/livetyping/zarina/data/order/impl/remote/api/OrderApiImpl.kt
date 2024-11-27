package ru.livetyping.zarina.data.order.impl.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.livetyping.zarina.core.network.di.ZarinaApi
import ru.livetyping.zarina.core.network.di.ZarinaApiType
import ru.livetyping.zarina.data.order.impl.remote.api.dto.GetOrdersDto
import javax.inject.Inject

internal class OrderApiImpl @Inject constructor(
    @ZarinaApi(ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
) : OrderApi {
    override suspend fun getOrders(page: Int): GetOrdersDto {
        return httpClient.get("/api/v1/orders") {
            parameter("page", page)
        }.body()
    }
}
