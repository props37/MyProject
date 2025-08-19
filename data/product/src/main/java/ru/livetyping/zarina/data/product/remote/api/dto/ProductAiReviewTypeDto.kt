package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import timber.log.Timber

@Serializable
@JvmInline
public value class ProductAiReviewTypeDto(public val value: String) {
    public fun toProductAiReviewType(): ProductAiReviews.FocusType? = when (value) {
        VALUE_POSITIVE -> ProductAiReviews.FocusType.POSITIVE
        VALUE_NEGATIVE -> ProductAiReviews.FocusType.NEGATIVE
        else -> {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to ProductAiReviewType")
            null
        }
    }

    public companion object {
        private const val VALUE_POSITIVE = "positive"
        private const val VALUE_NEGATIVE = "negative"

        private const val TAG = "ProductAiReviewTypeDto"
    }
}