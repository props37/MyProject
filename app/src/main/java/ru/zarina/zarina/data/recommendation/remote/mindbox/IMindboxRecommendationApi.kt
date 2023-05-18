package ru.zarina.zarina.data.recommendation.remote.mindbox

import ru.zarina.zarina.data.recommendation.remote.mindbox.dto.RecommendationRequestBody
import ru.zarina.zarina.data.recommendation.remote.mindbox.dto.RecommendationsResponseDto

interface IMindboxRecommendationApi {

    suspend fun getProductRecommendations(body: RecommendationRequestBody): RecommendationsResponseDto

}
