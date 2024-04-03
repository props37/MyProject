package ru.livetyping.zarina.domain.product

import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.filter.Filters

data class CategoryProductInfo(
    val categoryId: Category.Id,
    val productCount: Int,
    val availableFilters: Filters,
)
