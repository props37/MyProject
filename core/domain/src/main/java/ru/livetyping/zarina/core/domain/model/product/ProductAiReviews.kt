package ru.livetyping.zarina.core.domain.model.product

public data class ProductAiReviews(
    val productId: Product.Id,
    val description: String?,
    val reviewsCount: Int,
    val longDescription: String?,
    val tags: List<Tag>,
) {
    public enum class FocusType {
        POSITIVE,
        NEGATIVE,
    }

    public data class Tag(
        val text: String,
        val focus: FocusType,
        val count: Int?,
    )
}
