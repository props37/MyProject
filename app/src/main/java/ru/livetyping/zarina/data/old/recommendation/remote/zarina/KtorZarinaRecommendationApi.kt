package ru.livetyping.zarina.data.old.recommendation.remote.zarina

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import ru.livetyping.zarina.data.old.remote.zarina.dto.ProductBatchDto
import ru.livetyping.zarina.di.old.Qualifiers

@Factory
class KtorZarinaRecommendationApi(
    @Named(Qualifiers.Api.ZARINA_RESTRICTED)
    private val client: HttpClient,
) : IZarinaRecommendationApi {
    override suspend fun getProductRecommendations(productId: String): ProductBatchDto {
        val response = client.get("/api/products/$productId/similar_products/")
        return response.body()
    }

    override suspend fun getPersonalRecommendations(): ProductBatchDto {
        val response = client.get("/api/personal-recommendation/") {
            parameter("group", "catalog")
        }
        return response.body()
    }
}
