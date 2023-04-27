package ru.zarina.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.zarina.zarina.data.product.remote.api.dto.CompleteLookDto
import ru.zarina.zarina.data.product.remote.api.dto.DeliveryInfoDto
import ru.zarina.zarina.data.product.remote.api.dto.ProductDto
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaProductApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaProductApi {

    override suspend fun getProduct(id: String): ProductDto {
        val response = client.get("/api/products/$id")
        return response.body()
    }

    override suspend fun getCompleteLook(id: String): CompleteLookDto {
        val response = client.get("/api/products/$id/total_look") {
            parameter("with-articles", "")
        }
        return response.body()
    }

    override suspend fun getDeliveryInfo(id: String): DeliveryInfoDto {
        val response = client.get("/api/products/$id/delivery-info")
        return response.body()
    }
}
