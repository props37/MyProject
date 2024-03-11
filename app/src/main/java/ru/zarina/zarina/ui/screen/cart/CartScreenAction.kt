package ru.zarina.zarina.ui.screen.cart

import ru.zarina.zarina.domain.geography.City

sealed class CartScreenAction {
    data object GoToCatalogClicked : CartScreenAction()

    data class CityClicked(val city: City?) : CartScreenAction()
}
