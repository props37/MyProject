package ru.livetyping.zarina.core.domain.model.product

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters

public data class CategoryProductInfo(
    val categoryId: Category.Id,
    val productCount: Int,
    val availableFilters: ProductFilters,
)
