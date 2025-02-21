package ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter

import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem

internal sealed interface ListFilterScreenAction {
    data object BackClicked : ListFilterScreenAction

    data class AppliedClicked(
        val filter: ProductListFilter<ProductListFilterItem>,
    ) : ListFilterScreenAction
}
