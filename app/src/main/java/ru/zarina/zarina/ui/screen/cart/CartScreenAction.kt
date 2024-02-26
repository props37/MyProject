package ru.zarina.zarina.ui.screen.cart

sealed class CartScreenAction {
    data object GoToCatalogClicked : CartScreenAction()
}
