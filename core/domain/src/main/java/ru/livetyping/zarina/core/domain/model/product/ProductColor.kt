package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.common.Color

public data class ProductColor(
    val id: Id,
    val name: String,
    val color: Color,
    val productId: Product.Id,
) {
    @JvmInline
    public value class Id(public val value: String)
}
