package ru.zarina.zarina.ui.screen.cityselector

import ru.zarina.zarina.domain.rework.geography.City

sealed class CitySelectorScreenResult {
    data object ScreenClosed : CitySelectorScreenResult()

    data class CitySelected(val city: City) : CitySelectorScreenResult()
}
