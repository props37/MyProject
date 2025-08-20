package ru.livetyping.zarina.data.product.remote.api.dto

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import timber.log.Timber

@Serializable
@JvmInline
internal value class ProductAiReviewTagFocusDto(val value: String) {
    fun toProductAiReviewTagFocusType(): ProductAiReviews.Tag.Focus? = when (value) {
        VALUE_POSITIVE -> ProductAiReviews.Tag.Focus.POSITIVE
        VALUE_NEGATIVE -> ProductAiReviews.Tag.Focus.NEGATIVE
        else -> {
            Timber.tag(TAG).e("Ignore $this because it can't be mapped to ProductAiReviewType")
            null
        }
    }

    companion object {
        private const val VALUE_POSITIVE = "positive"
        private const val VALUE_NEGATIVE = "negative"

        private const val TAG = "ProductAiReviewTypeDto"
    }
}