package ru.livetyping.zarina.core.uicomponent.filtration.model

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter

public sealed interface ProductFiltrationEvent {
    public data class FilterChanged(val filter: ProductFilter<*>) : ProductFiltrationEvent

    public data class FilterClicked(val filter: ProductFilter<*>) : ProductFiltrationEvent

    public data object ShowProductsClicked : ProductFiltrationEvent

    public data object ErrorRefreshClicked : ProductFiltrationEvent
}
