package ru.zarina.zarina.data.recommendation.remote

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.recommendation.remote.zarina.IZarinaRecommendationApi
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType

@Factory
class ZarinaRecommendationRemoteSource(
    private val api: IZarinaRecommendationApi,
) : IRecommendationRemoteSource {

    override suspend fun getRecommendations(type: RecommendationType): List<Product> {
        return when (type) {
            is RecommendationType.Similar -> getSimilarRecommendations(type.product)
        }
    }

    private suspend fun getSimilarRecommendations(product: Product): List<Product> {
        return api.getProductRecommendations(product.id.value).toDomain()
    }

}
