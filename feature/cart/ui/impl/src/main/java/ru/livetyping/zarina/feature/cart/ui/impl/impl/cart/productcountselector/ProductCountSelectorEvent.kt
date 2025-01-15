package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector

import ru.livetyping.zarina.core.domain.model.cart.CartProduct

internal interface ProductCountSelectorEvent {
    data object DismissRequested : ProductCountSelectorEvent

    data class CountItemClicked(
        val product: CartProduct,
        val countItem: ProductCountItem,
    ) : ProductCountSelectorEvent
}
