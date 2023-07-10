package ru.zarina.zarina.domain

data class FilteredProducts(
    val products: List<Product>,
    val filtration: Filtration,
)
