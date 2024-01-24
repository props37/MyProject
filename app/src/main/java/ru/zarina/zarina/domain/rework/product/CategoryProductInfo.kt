package ru.zarina.zarina.domain.rework.product

import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.filter.Filters

data class CategoryProductInfo(
    val categoryId: Category.Id,
    val productCount: Int,
    val availableFilters: Filters,
    val appliedFilters: Unit, // // TODO: [High] Implement? Is needed?
)
