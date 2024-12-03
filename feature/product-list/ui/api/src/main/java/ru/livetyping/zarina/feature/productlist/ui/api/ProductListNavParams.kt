package ru.livetyping.zarina.feature.productlist.ui.api

import ru.livetyping.zarina.core.domain.model.category.Category

public data class ProductListNavParams(
    val categoryId: Category.Id,
) {
    public fun toNavEntry(): ProductListNavEntry {
        return ProductListNavEntry(
            categoryId = categoryId.value,
        )
    }
}
