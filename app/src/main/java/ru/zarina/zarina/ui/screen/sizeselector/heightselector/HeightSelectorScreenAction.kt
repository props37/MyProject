package ru.zarina.zarina.ui.screen.sizeselector.heightselector

import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductOffer

sealed class HeightSelectorScreenAction {
    data object ScreenClosed : HeightSelectorScreenAction()

    data object SizeSelectorFlowClosed : HeightSelectorScreenAction()

    data class OfferClicked(
        val product: Product,
        val offer: ProductOffer,
    ) : HeightSelectorScreenAction()
}
