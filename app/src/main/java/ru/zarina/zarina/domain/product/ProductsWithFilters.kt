package ru.zarina.zarina.domain.product

import ru.zarina.zarina.domain.filter.Filters

data class ProductsWithFilters(
    val products: List<Product>,
    val filters: Filters,
)
