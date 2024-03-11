package ru.zarina.zarina.data.old.recommendation

import org.koin.core.annotation.Factory
import ru.zarina.zarina.data.old.recommendation.remote.IRecommendationRemoteSource
import ru.zarina.zarina.domain.old.RecommendationType

@Factory
class RecommendationRepository(
    private val remote: IRecommendationRemoteSource,
) : IRecommendationRepository {

    override fun getRecommendations(type: RecommendationType) = remote.getRecommendations(type)

}
