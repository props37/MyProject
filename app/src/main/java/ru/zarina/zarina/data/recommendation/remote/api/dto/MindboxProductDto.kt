package ru.zarina.zarina.data.recommendation.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.zarina.zarina.domain.Product

@Serializable
data class MindboxProductDto(
    @SerialName("ids")
    val ids: RecommendationIdsDto,
)

fun Product.toMindboxProductDto(): MindboxProductDto = MindboxProductDto(
    ids = RecommendationIdsDto(
        catalog = "" // TODO
    )
)
