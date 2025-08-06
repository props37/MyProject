package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable

internal sealed class AiReviewsState {
    @Immutable
    data class Content(
        val rating: Float,
        val reviewsCount: Int,
        val tags: List<AiReviewTag>
    ): AiReviewsState()

    @Immutable
    data class AiReviewTag(
        val text: String,
        val focus: String,
    )
}