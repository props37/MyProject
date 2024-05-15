package ru.livetyping.zarina.presentation.screen.sizeselector.heightselector

import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer

sealed class HeightSelectorScreenAction {
    data object ScreenClosed : HeightSelectorScreenAction()

    data object SizeSelectorFlowClosed : HeightSelectorScreenAction()

    data class OfferClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : HeightSelectorScreenAction()
}
