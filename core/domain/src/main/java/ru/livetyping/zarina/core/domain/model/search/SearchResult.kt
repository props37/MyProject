package ru.livetyping.zarina.core.domain.model.search

import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

// Marked as stable on config/compose/stability_config.txt
public data class SearchResult(
    val products: List<ProductShort>,
    val productTotalCount: Int,
    val availableFilters: ProductFilters,
    val offset: Int,
)
