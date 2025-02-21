package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model

import ru.livetyping.zarina.core.domain.model.product.filter.list.ProductListFilterItem

public sealed interface ListFilterEvent {
    public data class ItemClicked(val item: ProductListFilterItem) : ListFilterEvent

    public data object ApplyClicked : ListFilterEvent
}
