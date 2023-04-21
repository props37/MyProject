package ru.zarina.zarina.data.product.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.product.remote.api.dto.ProductDto
import javax.inject.Inject

class KtorZarinaProductApi @Inject constructor(
    private val client: HttpClient,
) : IZarinaProductApi {

    override suspend fun getProduct(id: String): ProductDto {
        val response = client.get("/api/products/$id")
        return response.body()
    }

}
