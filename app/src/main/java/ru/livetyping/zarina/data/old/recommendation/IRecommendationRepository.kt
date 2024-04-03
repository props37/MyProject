package ru.livetyping.zarina.data.old.recommendation

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.old.Product
import ru.livetyping.zarina.domain.old.RecommendationType

interface IRecommendationRepository {

    fun getRecommendations(type: RecommendationType): Flow<List<Product>>

}
