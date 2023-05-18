package ru.zarina.zarina.data.recommendation

import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType

interface IRecommendationRepository {

    suspend fun getRecommendations(type: RecommendationType): List<Product>

}
