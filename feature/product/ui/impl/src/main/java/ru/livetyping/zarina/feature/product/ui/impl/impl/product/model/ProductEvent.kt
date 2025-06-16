package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductColor
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

internal sealed interface ProductEvent {
    data object BackClicked : ProductEvent

    data object ShareClicked : ProductEvent

    data class ProductColorClicked(val color: ProductColor) : ProductEvent

    data class ProductClicked(val product: Product) : ProductEvent

    data class CheckAvailabilityInStoresClicked(val product: Product) : ProductEvent

    data class AddProductToWishlistClicked(val product: Product) : ProductEvent

    data object SizeTableClicked : ProductEvent

    data object SelectSizeClicked : ProductEvent

    data object SelectHeightClicked : ProductEvent

    data class SizeSelected(val offer: ProductOffer, val type: SizeSelectorType) : ProductEvent

    data object ProductRefreshTriggered : ProductEvent

    data object TotalLookProductRefreshTriggered : ProductEvent

    data object SimilarProductRefreshTriggered : ProductEvent

    data object SizeSelectorDismissed : ProductEvent
}
