package ru.livetyping.zarina.core.uicomponent.filtration.model

import ru.livetyping.zarina.core.domain.model.product.filter.ProductFilter

public sealed interface FiltrationEvent {
    public data class FilterChanged(val filter: ProductFilter<*>) : FiltrationEvent

    public data class FilterClicked(val filter: ProductFilter<*>) : FiltrationEvent

    public data object ShowProductsClicked : FiltrationEvent

    public data object FiltrationErrorRefreshClicked : FiltrationEvent
}
