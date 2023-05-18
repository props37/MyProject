package ru.zarina.zarina.data.recommendation

import ru.zarina.zarina.data.recommendation.remote.IRecommendationRemoteSource
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.RecommendationType
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val remote: IRecommendationRemoteSource,
) : IRecommendationRepository {

    override suspend fun getRecommendations(type: RecommendationType): List<Product> =
        remote.getRecommendations(type)

}
