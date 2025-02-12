package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilters
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry

@Serializable
internal class ProductListNavEntry private constructor(
    override val categoryId: String,
    override val filters: ProductFiltersParcelable?,
): ProductListNavEntry() {
    companion object {
        fun create(
            categoryId: Category.Id,
            filters: ProductFilters? = null,
        ): ProductListNavEntry {
            return ProductListNavEntry(
                categoryId = categoryId.value,
                filters = filters?.let { ProductFiltersParcelable.from(it) },
            )
        }
    }
}
