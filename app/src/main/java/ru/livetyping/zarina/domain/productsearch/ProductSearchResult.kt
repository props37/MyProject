package ru.livetyping.zarina.domain.productsearch

import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.product.ProductItem

data class ProductSearchResult(
    val products: List<ProductItem>,
    val productTotalCount: Int,
    val filters: Filters,
    val offset: Int,
)
