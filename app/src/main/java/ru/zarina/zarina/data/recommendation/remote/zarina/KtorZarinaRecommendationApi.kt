package ru.zarina.zarina.data.recommendation.remote.zarina

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.zarina.zarina.data.common.remote.zarina.dto.ProductBatchDto
import ru.zarina.zarina.di.Qualifiers

@Factory
class KtorZarinaRecommendationApi(
    @Named(Qualifiers.Authorization.TOKEN)
    private val client: HttpClient,
) : IZarinaRecommendationApi {
    override suspend fun getProductRecommendations(productId: String): ProductBatchDto {
        val response = client.get("/api/products/$productId/similar_products/")
        return response.body()
    }
}
