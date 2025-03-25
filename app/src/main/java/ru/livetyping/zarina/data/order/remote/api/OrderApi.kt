package ru.livetyping.zarina.data.order.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import ru.livetyping.zarina.data.order.remote.api.dto.CreateOrderDto
import ru.livetyping.zarina.data.order.remote.api.dto.CreateOrderRequestBody
import ru.livetyping.zarina.data.order.remote.api.dto.GetOrdersDto
import ru.livetyping.zarina.data.order.remote.api.dto.OrderDto
import ru.livetyping.zarina.data.order.remote.api.exception.OrderCreationExceptionConverter
import ru.livetyping.zarina.di.Qualifiers
import ru.livetyping.zarina.domain.order.Order
import ru.livetyping.zarina.domain.order.OrderCreationParams
import ru.livetyping.zarina.util.library.ktor.setJsonBody
import javax.inject.Inject

class OrderApi @Inject constructor(
    @Qualifiers.ZarinaApi(Qualifiers.ZarinaApiType.AUTHORIZED)
    private val httpClient: HttpClient,
    private val orderCreationExceptionConverter: OrderCreationExceptionConverter,
) {
    suspend fun getOrders(page: Int): GetOrdersDto {
        return httpClient.get("/api/v1/orders") {
            parameter("page", page)
        }.body()
    }

    suspend fun getOrder(orderId: Order.Id): List<OrderDto> {
        return httpClient.get("/api/v1/orders/${orderId.value}").body()
    }

    suspend fun createOrder(params: OrderCreationParams): CreateOrderDto {
        val body = CreateOrderRequestBody.from(params)
        return orderCreationExceptionConverter {
            httpClient.post("/api/orders/") {
                setJsonBody(body)
            }.body()
        }
    }

    suspend fun cancelOrder(orderId: Order.Id) {
        httpClient.delete("/api/orders/${orderId.value}")
    }
}
