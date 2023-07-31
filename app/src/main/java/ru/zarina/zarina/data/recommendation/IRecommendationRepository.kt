package ru.zarina.zarina.data.recommendation

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType

interface IRecommendationRepository {

    fun getRecommendations(type: RecommendationType): Flow<List<Product>>

}
