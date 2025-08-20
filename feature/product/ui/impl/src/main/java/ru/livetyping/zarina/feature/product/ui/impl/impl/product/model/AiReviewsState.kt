package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews

@Immutable
public data class AiReviewsState(
    val longDescription: String?,
    val tags: List<Tag>,
) {
    @Immutable
    public data class Tag(
        val text: String,
        val focus: ProductAiReviews.Tag.Focus,
    )

    internal companion object {
        fun from(productAiReviews: ProductAiReviews): AiReviewsState? {
            return if (productAiReviews.longDescription != null && productAiReviews.tags.isNotEmpty()) {
                AiReviewsState(
                    longDescription = productAiReviews.longDescription,
                    tags = productAiReviews.tags
                        .filter { it.focus == ProductAiReviews.Tag.Focus.POSITIVE }
                        .map { Tag(text = it.text, focus = it.focus) }
                )
            } else null
        }
    }
}