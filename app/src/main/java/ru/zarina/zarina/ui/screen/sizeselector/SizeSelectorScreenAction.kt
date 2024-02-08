package ru.zarina.zarina.ui.screen.sizeselector

import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer

sealed class SizeSelectorScreenAction {
    data class SizeClicked(
        val product: Product,
        val offers: List<ProductOffer>,
    ) : SizeSelectorScreenAction()
}
