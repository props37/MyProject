package ru.zarina.zarina.data.recommendation

import ru.zarina.zarina.data.recommendation.remote.IRecommendationRemoteSource
import javax.inject.Inject

class RecommendationRepository @Inject constructor(
    private val remote: IRecommendationRemoteSource,
) : IRecommendationRepository
