package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilter
import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem

@Immutable
public data class ListFilterState(
    val filter: ProductListFilter<ProductListFilterItem>,
    val isApplyButtonVisible: Boolean,
    val cityHeader: City?,
)
