package ru.livetyping.zarina.domain.old

data class FilteredProducts(
    val products: List<Product>,
    val filtration: Filtration,
)
