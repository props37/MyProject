package ru.zarina.zarina.data.recommendation.remote

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType

interface IRecommendationRemoteSource {

    suspend fun getRecommendations(type: RecommendationType): List<Product.Id>

}
