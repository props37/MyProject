package ru.livetyping.zarina.presentation.screen.product

import ru.livetyping.zarina.domain.product.Product

sealed class ProductScreenAction {
    data object ScreenClosed : ProductScreenAction()

    data class CheckAvailabilityInStoresClicked(val product: Product) : ProductScreenAction()

    data class ProductClicked(val product: Product) : ProductScreenAction()

    data class AddProductToCartClicked(val product: Product) : ProductScreenAction()

    data class SubscribeToProductClicked(val product: Product) : ProductScreenAction()
}
