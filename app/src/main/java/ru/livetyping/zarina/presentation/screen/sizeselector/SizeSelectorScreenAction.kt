package ru.livetyping.zarina.presentation.screen.sizeselector

import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductOffer

sealed class SizeSelectorScreenAction {
    data object ScreenClosed : SizeSelectorScreenAction()

    data class SizeClicked(
        val product: Product,
        val offers: List<ProductOffer>,
    ) : SizeSelectorScreenAction()
}
