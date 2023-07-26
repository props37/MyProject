package ru.zarina.zarina.data.recommendation

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.recommendation.remote.IRecommendationRemoteSource
import ru.zarina.zarina.domain.RecommendationType

@Factory
class RecommendationRepository(
    private val remote: IRecommendationRemoteSource,
) : IRecommendationRepository {

    override fun getRecommendations(type: RecommendationType) = remote.getRecommendations(type)

}
