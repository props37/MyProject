package ru.zarina.zarina.data.old.recommendation

import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.old.Product
import ru.zarina.zarina.domain.old.RecommendationType

interface IRecommendationRepository {

    fun getRecommendations(type: RecommendationType): Flow<List<Product>>

}
