package ru.zarina.zarina.domain.rework.product

import ru.zarina.zarina.domain.common.Color

data class ProductColor(
    val id: Id,
    val name: String,
    val color: Color,
    val productId: Product.Id,
) {
    @JvmInline
    value class Id(val value: String)
}
