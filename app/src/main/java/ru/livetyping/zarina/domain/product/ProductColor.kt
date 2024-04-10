package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.common.Color

data class ProductColor(
    val id: Id,
    val name: String,
    val color: Color,
    val productId: Product.Id,
) {
    @JvmInline
    value class Id(val value: String)
}
