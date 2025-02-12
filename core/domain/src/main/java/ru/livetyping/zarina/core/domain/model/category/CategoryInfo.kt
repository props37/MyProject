package ru.livetyping.zarina.core.domain.model.category

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

public data class CategoryInfo(
    val categoryId: Category.Id,
    val productCount: Int,
    val availableFilters: ProductFilters,
)
