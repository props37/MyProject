package ru.livetyping.zarina.data.old.recommendation.remote.zarina

import ru.livetyping.zarina.data.old.remote.zarina.dto.ProductBatchDto

interface IZarinaRecommendationApi {
    suspend fun getProductRecommendations(productId: String): ProductBatchDto
    suspend fun getPersonalRecommendations(): ProductBatchDto
}
