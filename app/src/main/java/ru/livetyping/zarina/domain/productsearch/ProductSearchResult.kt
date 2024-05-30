package ru.livetyping.zarina.domain.productsearch

import ru.livetyping.zarina.domain.product.ProductItem

data class ProductSearchResult(
    val products: List<ProductItem>,
    val productTotalCount: Int,
    val offset: Int,
)
