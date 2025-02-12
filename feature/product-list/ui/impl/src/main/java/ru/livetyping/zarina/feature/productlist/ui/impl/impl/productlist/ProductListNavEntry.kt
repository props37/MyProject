package ru.livetyping.zarina.feature.productlist.ui.impl.impl.productlist

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.uimodel.product.filter.ProductFiltersParcelable
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry

@Serializable
internal class ProductListNavEntry private constructor(
    override val categoryId: String,
    override val filters: ProductFiltersParcelable?,
): ProductListNavEntry()
