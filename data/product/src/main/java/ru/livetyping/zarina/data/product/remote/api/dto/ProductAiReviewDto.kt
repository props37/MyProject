package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull

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
    val tableTagsData: List<TagDto>? = null,
) {
    fun toProductAiReviews(): ProductAiReviews {
        checkPropertyNotNull(externalId) { "externalId" }
        return ProductAiReviews(
            productId = Product.Id(externalId),
            description = checkPropertyNotNull(description) { "description" },
            reviewsCount = checkPropertyNotNull(reviewsCount) { "reviewsCount" },
            longDescription = checkPropertyNotNull(longDescription) { "longDescription" },
            tags = tableTagsData?.map { it.toTag() } ?: emptyList(),
        )
    }

    @Serializable
    data class TagDto(
        @SerialName("tagName")
        val tagName: String? = null,

        @SerialName("tagFocus")
        val tagFocus: String? = null,

        @SerialName("tagCount")
        val tagCount: String? = null,
    ) {
        fun toTag(): ProductAiReviews.Tag {
            return ProductAiReviews.Tag(
                name = checkPropertyNotNull(tagName) { "tagName" },
                focus = checkPropertyNotNull(tagFocus) { "tagFocus" },
                count = checkPropertyNotNull(tagCount) { "tagCount" }
            )
        }
    }
}