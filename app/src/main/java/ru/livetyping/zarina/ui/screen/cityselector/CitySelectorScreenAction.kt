package ru.livetyping.zarina.ui.screen.cityselector

import ru.livetyping.zarina.domain.geography.City

sealed class CitySelectorScreenAction {
    data object ScreenClosed : CitySelectorScreenAction()

    data class CitySelected(val city: City) : CitySelectorScreenAction()
}
