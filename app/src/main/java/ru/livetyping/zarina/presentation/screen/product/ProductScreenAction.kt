package ru.livetyping.zarina.presentation.screen.product

import ru.livetyping.zarina.domain.product.Product

sealed class ProductScreenAction {
    data object ScreenClosed : ProductScreenAction()

    data class ProductClicked(val product: Product) : ProductScreenAction()
}
