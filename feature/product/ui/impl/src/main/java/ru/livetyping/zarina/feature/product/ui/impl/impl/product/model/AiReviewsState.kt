package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews

@Immutable
public data class AiReviewsState(
    val rating: Float,
    val reviewsCount: Int,
    val tags: List<AiReviewTag>
) {
    @Immutable
    public data class AiReviewTag(
        val text: String,
        val focus: String,
    )

    internal companion object {
        fun from (productAiReviews: ProductAiReviews): AiReviewsState {
            return AiReviewsState(
                rating = 0f,
                reviewsCount = productAiReviews.reviewsCount,
                tags = productAiReviews.tags.map { tag ->
                    AiReviewTag(
                        text = tag.name,
                        focus = tag.focus
                    )
                }
            )
        }
    }
}