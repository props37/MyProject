package ru.zarina.zarina.data.recommendation.remote.api

import io.ktor.client.HttpClient
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorMindboxRecommendationApi @Inject constructor(
    @Authorization(Authorization.Type.MINDBOX_SECRET)
    private val client: HttpClient,
) : IMindboxRecommendationApi
