package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import ru.livetyping.zarina.core.network.util.checkPropertyNotNull
import timber.log.Timber

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
    fun toProductAiReviews(): ProductAiReviews? {
        return try {
            checkPropertyNotNull(externalId) { "externalId" }
            ProductAiReviews(
                productId = Product.Id(externalId),
                description = description,
                reviewsCount = checkPropertyNotNull(reviewsCount) { "reviewsCount" },
                longDescription = longDescription,
                tags = tableTagsData?.map { it.toTag() } ?: emptyList(),
            )
        } catch (e: Exception) {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to ProductAiReview")
            null
        }
    }

    @Serializable
    data class TagDto(
        @SerialName("tagName")
        val tagName: String? = null,

        @SerialName("tagFocus")
        val tagFocus: ProductAiReviewTypeDto? = null,

        @SerialName("tagCount")
        val tagCount: Int? = null,
    ) {
        fun toTag(): ProductAiReviews.Tag {
            return ProductAiReviews.Tag(
                text = checkPropertyNotNull(tagName?.takeIf { it.isNotBlank() }) { "tagName" },
                focus = checkPropertyNotNull(tagFocus?.toProductAiReviewType()) { "tagFocus" },
                count = tagCount
            )
        }
    }

    private companion object {
        private const val TAG = "ProductAiReviewDto"
    }
}