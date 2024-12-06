package ru.livetyping.zarina.core.domain.model.product.filter

import ru.livetyping.zarina.core.domain.model.product.ProductShort

public data class ProductsWithFilters(
    val products: List<ProductShort>,
    val filters: ProductFilters,
)
