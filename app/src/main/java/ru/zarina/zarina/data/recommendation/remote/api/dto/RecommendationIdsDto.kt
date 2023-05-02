package ru.zarina.zarina.data.recommendation.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecommendationIdsDto(
    @SerialName("mindboxId")
    val mindboxId: Long? = null,
    @SerialName("catalog")
    val catalog: String? = null,
)
