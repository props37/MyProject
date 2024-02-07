package ru.zarina.zarina.ui.screen.sizeselector

import ru.zarina.zarina.domain.rework.product.ProductOffer

sealed class SizeSelectorScreenAction {
    data class SizeClicked(val offers: List<ProductOffer>) : SizeSelectorScreenAction()
}
