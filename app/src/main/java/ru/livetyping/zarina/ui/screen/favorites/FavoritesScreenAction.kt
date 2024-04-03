package ru.livetyping.zarina.ui.screen.favorites

import ru.livetyping.zarina.domain.product.Product

sealed class FavoritesScreenAction {
    data object GoToCatalogClicked : FavoritesScreenAction()

    data class AddProductToCartClicked(val product: Product) : FavoritesScreenAction()

    data class SubscribeToProductClicked(val product: Product) : FavoritesScreenAction()
}
