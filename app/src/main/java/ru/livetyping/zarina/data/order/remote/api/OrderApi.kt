package ru.livetyping.zarina.data.order.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.livetyping.zarina.data.order.remote.api.dto.GetOrdersDto
import ru.livetyping.zarina.data.order.remote.api.dto.GetOrdersRequestBody
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class OrderApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApis.AUTHORIZED)
    private val httpClient: HttpClient,
) {
    suspend fun getOrders(page: Int): GetOrdersDto {
        val body = GetOrdersRequestBody(page)
        return httpClient.get("/api/v1/orders") {
            setJsonBody(body)
        }.body()
    }
}
