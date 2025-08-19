package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews

@Immutable
public data class AiReviewsState(
    val reviewsCount: Int,
    val tags: List<AiReviewTag>
) {
    @Immutable
    public data class AiReviewTag(
        val text: String,
        val focus: ProductAiReviews.FocusType,
    )

    internal companion object {
        fun from (productAiReviews: ProductAiReviews): AiReviewsState {
            return AiReviewsState(
                reviewsCount = productAiReviews.reviewsCount,
                tags = productAiReviews.tags.map { tag ->
                    AiReviewTag(
                        text = tag.text,
                        focus = tag.focus
                    )
                }
            )
        }
    }
}