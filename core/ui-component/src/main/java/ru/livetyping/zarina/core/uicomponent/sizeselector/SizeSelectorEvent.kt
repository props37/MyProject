package ru.livetyping.zarina.core.uicomponent.sizeselector

import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

public sealed interface SizeSelectorEvent {
    public data object DismissRequested : SizeSelectorEvent

    public data class SizeSelected(
        val product: Product,
        val offer: ProductOffer,
    ) : SizeSelectorEvent
}
