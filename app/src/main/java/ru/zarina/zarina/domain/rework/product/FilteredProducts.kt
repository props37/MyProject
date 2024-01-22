package ru.zarina.zarina.domain.rework.product

import ru.zarina.zarina.domain.rework.filter.Filters

data class FilteredProducts(
    val products: List<Product>,
    val filters: Filters,
)
