package ru.zarina.zarina.data.recommendation.remote.api

import ru.zarina.zarina.data.recommendation.remote.api.dto.RecommendationRequestDto
import ru.zarina.zarina.data.recommendation.remote.api.dto.RecommendationsResponseDto

interface IMindboxRecommendationApi {

    suspend fun getProductRecommendations(body: RecommendationRequestDto): RecommendationsResponseDto

}
