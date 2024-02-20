package ru.zarina.zarina.ui.screen.cityselector

import ru.zarina.zarina.domain.rework.geography.City

sealed class CitySelectorScreenAction {
    data object ScreenClosed : CitySelectorScreenAction()

    data class CitySelected(val city: City) : CitySelectorScreenAction()
}
