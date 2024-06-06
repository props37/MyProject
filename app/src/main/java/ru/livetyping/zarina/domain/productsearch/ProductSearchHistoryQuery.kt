package ru.livetyping.zarina.domain.productsearch

data class ProductSearchHistoryQuery(
    val text: String,
    val timestampMillis: Long,
)
