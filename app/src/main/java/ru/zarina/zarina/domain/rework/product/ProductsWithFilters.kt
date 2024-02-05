package ru.zarina.zarina.domain.rework.product

import ru.zarina.zarina.domain.rework.filter.Filters

data class ProductsWithFilters(
    val products: List<Product>,
    val filters: Filters,
)
