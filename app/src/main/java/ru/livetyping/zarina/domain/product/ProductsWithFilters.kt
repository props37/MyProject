package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.filter.Filters

data class ProductsWithFilters(
    val products: List<Product>,
    val filters: Filters,
)
