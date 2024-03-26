package ru.zarina.zarina.ui.screen.favorites

import ru.zarina.zarina.domain.product.Product

sealed class FavoritesScreenAction {
    data object GoToCatalogClicked : FavoritesScreenAction()

    data class AddProductToCartClicked(val product: Product) : FavoritesScreenAction()
}
