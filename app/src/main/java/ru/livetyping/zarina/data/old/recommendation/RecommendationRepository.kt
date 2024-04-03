package ru.livetyping.zarina.data.old.recommendation

import org.koin.core.annotation.Factory
import ru.livetyping.zarina.data.old.recommendation.remote.IRecommendationRemoteSource
import ru.livetyping.zarina.domain.old.RecommendationType

@Factory
class RecommendationRepository(
    private val remote: IRecommendationRemoteSource,
) : IRecommendationRepository {

    override fun getRecommendations(type: RecommendationType) = remote.getRecommendations(type)

}
