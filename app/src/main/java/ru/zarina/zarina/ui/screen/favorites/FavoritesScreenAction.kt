package ru.zarina.zarina.ui.screen.favorites

sealed class FavoritesScreenAction {
    data object GoToCatalogClicked : FavoritesScreenAction()
}
