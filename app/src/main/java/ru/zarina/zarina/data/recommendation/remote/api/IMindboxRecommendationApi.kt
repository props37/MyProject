package ru.zarina.zarina.data.recommendation.remote.api

import ru.zarina.zarina.data.recommendation.remote.api.dto.RecommendationRequestBody
import ru.zarina.zarina.data.recommendation.remote.api.dto.RecommendationsResponseDto

interface IMindboxRecommendationApi {

    suspend fun getProductRecommendations(body: RecommendationRequestBody): RecommendationsResponseDto

}
