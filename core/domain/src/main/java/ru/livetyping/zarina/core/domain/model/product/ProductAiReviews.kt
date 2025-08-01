package ru.livetyping.zarina.core.domain.model.product

public data class ProductAiReviews(
    val productId: Product.Id,
    val description: String,
    val reviewsCount: Int,
    val longDescription: String,
    val tags: List<Tag>,
) {
    public data class Tag(
        val name: String,
        val focus: String,
        val count: String,
    )
}
