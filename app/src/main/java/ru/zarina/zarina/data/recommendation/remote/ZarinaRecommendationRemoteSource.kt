package ru.zarina.zarina.data.recommendation.remote

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.recommendation.remote.zarina.IZarinaRecommendationApi
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType

@Factory
class ZarinaRecommendationRemoteSource(
    private val api: IZarinaRecommendationApi,
) : IRecommendationRemoteSource {

    override fun getRecommendations(type: RecommendationType): Flow<List<Product>> {
        return flow {
            val value = when (type) {
                is RecommendationType.Similar -> getSimilarRecommendations(type.product)
            }
            emit(value)
        }
    }

    private suspend fun getSimilarRecommendations(product: Product): List<Product> {
        return api.getProductRecommendations(product.id.value).toDomain()
    }

}
