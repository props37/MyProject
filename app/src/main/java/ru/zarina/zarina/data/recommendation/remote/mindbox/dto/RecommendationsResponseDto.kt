package ru.zarina.zarina.data.recommendation.remote.mindbox.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.data.common.remote.mindbox.dto.MindboxResponseDto
import ru.zarina.zarina.domain.Product

@Serializable
data class RecommendationsResponseDto(
    @SerialName("status")
    override val status: String? = null,
    @SerialName("recommendations")
    val recommendations: List<RecommendationDto>? = null,
) : MindboxResponseDto {
    fun toDomain(): List<Product.Id> {
        return recommendations?.mapNotNull { it.toDomain() } ?: emptyList()
    }
}
