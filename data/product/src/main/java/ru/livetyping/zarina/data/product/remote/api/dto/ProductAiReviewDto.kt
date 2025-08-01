package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ProductAiReviewDto(

    @SerialName("externalId")
    val externalId: String? = null,

    @SerialName("description")
    val description: String? = null,

    @SerialName("reviewsCount")
    val reviewsCount: Int? = null,

    @SerialName("longDescription")
    val longDescription: String? = null,

    @SerialName("tableTagsData")
    val tableTagsData: List<tagDto>? = null,
) {
    @Serializable
    data class tagDto(
        @SerialName("tagName")
        val tagName: String? = null,

        @SerialName("tagFocus")
        val tagFocus: String? = null,

        @SerialName("tagCount")
        val tagCount: String? = null,
    )
}