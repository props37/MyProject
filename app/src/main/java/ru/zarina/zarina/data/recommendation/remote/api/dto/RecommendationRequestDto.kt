package ru.zarina.zarina.data.recommendation.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationRequestDto(
    @SerialName("limit")
    val limit: Int,
    @SerialName("product")
    val product: MindboxProductDto,
)
