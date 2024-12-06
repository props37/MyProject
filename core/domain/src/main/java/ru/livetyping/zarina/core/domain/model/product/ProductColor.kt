package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.common.Color

// Marked as stable on config/compose/stability_config.txt
public data class ProductColor(
    val id: Id,
    val name: String,
    val color: Color,
    val productId: Product.Id,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)
}
