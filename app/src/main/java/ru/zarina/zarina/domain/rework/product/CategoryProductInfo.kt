package ru.zarina.zarina.domain.rework.product

import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.rework.filter.Filters

data class CategoryProductInfo(
    val categoryId: Category.Id,
    val productCount: Int,
    val availableFilters: Filters,
)
