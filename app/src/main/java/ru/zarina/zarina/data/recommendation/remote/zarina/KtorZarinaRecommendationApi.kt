package ru.zarina.zarina.data.recommendation.remote.zarina

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductBatchDto
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorZarinaRecommendationApi @Inject constructor(
    @Authorization(Authorization.Type.TOKEN)
    private val client: HttpClient,
) : IZarinaRecommendationApi {
    override suspend fun getProductRecommendations(productId: String): ProductBatchDto {
        val response = client.get("/api/products/$productId/similar_products/")
        return response.body()
    }
}
