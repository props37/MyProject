package ru.zarina.zarina.domain.rework.product

data class FilteredProducts(
    val products: List<Product>,
    val filters: Unit,
)
