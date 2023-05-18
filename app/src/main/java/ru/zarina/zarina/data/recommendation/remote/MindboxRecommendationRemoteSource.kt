package ru.zarina.zarina.data.recommendation.remote

import ru.zarina.zarina.data.recommendation.remote.mindbox.IMindboxRecommendationApi
import ru.zarina.zarina.data.recommendation.remote.mindbox.dto.RecommendationRequestBody
import ru.zarina.zarina.data.recommendation.remote.mindbox.dto.toMindboxProductDto
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType
import javax.inject.Inject

class MindboxRecommendationRemoteSource @Inject constructor(
    private val api: IMindboxRecommendationApi,
) : IRecommendationRemoteSource {

    override suspend fun getRecommendations(type: RecommendationType): List<Product.Id> {
        return when (type) {
            is RecommendationType.Similar -> getSimilarRecommendations(type.product)
        }
    }

    private suspend fun getSimilarRecommendations(product: Product): List<Product.Id> {
        val productDto = product.toMindboxProductDto()
        val body = RecommendationRequestBody(
            limit = DEFAULT_RECOMMENDATION_COUNT,
            product = productDto,
        )
        return api.getProductRecommendations(body).toDomain()
    }

    companion object {
        private const val DEFAULT_RECOMMENDATION_COUNT = 10
    }

}
