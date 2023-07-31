package ru.zarina.zarina.data.recommendation.remote

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType

interface IRecommendationRemoteSource {

    fun getRecommendations(type: RecommendationType): Flow<List<Product>>

}
