package ru.zarina.zarina.data.old.recommendation.remote

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.RecommendationType

interface IRecommendationRemoteSource {

    fun getRecommendations(type: RecommendationType): Flow<List<Product>>

}
