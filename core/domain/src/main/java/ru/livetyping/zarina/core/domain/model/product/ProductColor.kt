package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.common.Color
import ru.livetyping.zarina.core.domain.model.common.Url

// Marked as stable on config/compose/stability_config.txt
public data class ProductColor(
    val id: Id,
    val name: String,
    val color: Color,
    val productId: Product.Id,
    val imageUrl: Url?,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Id(public val value: String)
}
