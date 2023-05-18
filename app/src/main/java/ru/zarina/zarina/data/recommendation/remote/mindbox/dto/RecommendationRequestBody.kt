package ru.zarina.zarina.data.recommendation.remote.mindbox.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationRequestBody(
    @SerialName("recommendation")
    val recommendation: RecommendationRequestDto,
) {

    constructor(
        limit: Int,
        product: MindboxProductDto,
    ) : this(
        RecommendationRequestDto(
            limit = limit,
            product = product,
        ),
    )

    @Serializable
    data class RecommendationRequestDto(
        @SerialName("limit")
        val limit: Int,
        @SerialName("product")
        val product: MindboxProductDto,
    )

}
