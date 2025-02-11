package ru.livetyping.zarina.feature.product.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.product.Product

internal sealed interface ProductSuggestionsEvent {
    data class ProductClicked(val product: Product) : ProductSuggestionsEvent

    data object ErrorRefreshClicked : ProductSuggestionsEvent
}
