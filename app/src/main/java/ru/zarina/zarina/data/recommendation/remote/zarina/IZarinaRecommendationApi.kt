package ru.zarina.zarina.data.recommendation.remote.zarina

import ru.zarina.zarina.data.common.remote.zarina.dto.ProductBatchDto

interface IZarinaRecommendationApi {
    suspend fun getProductRecommendations(productId: String): ProductBatchDto
}
