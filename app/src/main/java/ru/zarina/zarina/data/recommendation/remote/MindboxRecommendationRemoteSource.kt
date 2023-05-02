package ru.zarina.zarina.data.recommendation.remote

import ru.zarina.zarina.data.recommendation.remote.api.IMindboxRecommendationApi
import javax.inject.Inject

class MindboxRecommendationRemoteSource @Inject constructor(
    private val api: IMindboxRecommendationApi,
) : IRecommendationRemoteSource
