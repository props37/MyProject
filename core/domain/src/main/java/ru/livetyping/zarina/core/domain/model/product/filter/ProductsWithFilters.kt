package ru.livetyping.zarina.core.domain.model.product.filter

import ru.livetyping.zarina.core.domain.model.product.ProductShort

// Marked as stable on config/compose/stability_config.txt
public data class ProductsWithFilters(
    val products: List<ProductShort>,
    val filters: ProductFilters,
)
