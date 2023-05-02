package ru.zarina.zarina.data.recommendation.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product

@Serializable
data class RecommendationDto(
    @SerialName("ids")
    val ids: RecommendationIdsDto? = null,
) {
    fun toDomain() = ids?.catalog?.let { Product.Id(it) }
}
