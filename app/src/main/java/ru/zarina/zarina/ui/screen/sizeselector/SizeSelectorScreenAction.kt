package ru.zarina.zarina.ui.screen.sizeselector

import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.domain.product.ProductOffer

sealed class SizeSelectorScreenAction {
    data object ScreenClosed : SizeSelectorScreenAction()

    data class SizeClicked(
        val product: Product,
        val offers: List<ProductOffer>,
    ) : SizeSelectorScreenAction()
}
