package ru.livetyping.zarina.feature.productlist.ui.api

import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable

public data class ProductListNavParams(
    val categoryId: Category.Id,
    val filters: ProductFilters? = null,
) {
    public fun toNavEntry(): ProductListNavEntry {
        return ProductListNavEntry(
            categoryId = categoryId.value,
            filters = filters?.let { ProductFiltersParcelable.from(it) },
        )
    }
}
