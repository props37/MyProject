// ========== REVIEW FROM HERE ==========
package ru.livetyping.zarina.core.domain.model.product

public data class ProductAiReviews(
    val groupId: Product.GroupId,
    val description: String?,
    val longDescription: String?,
    val tags: List<Tag>,
) {
    public data class Tag(
        val text: String,
        val focus: Focus,
    ) {
        public enum class Focus {
            POSITIVE,
            NEGATIVE,
        }
    }
}
// ========== TO HERE ==========