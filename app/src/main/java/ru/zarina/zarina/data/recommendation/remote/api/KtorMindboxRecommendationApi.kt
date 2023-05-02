package ru.zarina.zarina.data.recommendation.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import ru.zarina.zarina.BuildConfig
import ru.zarina.zarina.data.recommendation.remote.api.dto.RecommendationRequestDto
import ru.zarina.zarina.data.recommendation.remote.api.dto.RecommendationsResponseDto
import ru.zarina.zarina.di.Authorization
import javax.inject.Inject

class KtorMindboxRecommendationApi @Inject constructor(
    @Authorization(Authorization.Type.MINDBOX_SECRET)
    private val client: HttpClient,
) : IMindboxRecommendationApi {

    override suspend fun getProductRecommendations(
        body: RecommendationRequestDto,
    ): RecommendationsResponseDto {
        val response = client.post {
            parameter(KEY_OPERATION, VALUE_OPERATION_SIMILAR)
            parameter(KEY_ENDPOINT_ID, BuildConfig.MINDBOX_ENDPOINT)
            contentType(ContentType.Application.Json)
            setBody(body)
        }
        return response.body()
    }

    companion object {
        private const val KEY_OPERATION = "operation"
        private const val KEY_ENDPOINT_ID = "endpointId"
        private const val VALUE_OPERATION_SIMILAR = "similar"
    }

}
