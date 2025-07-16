package ru.livetyping.zarina.feature.cityselector.ui.impl.screen

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface CitySelectorScreenAction {
    data object BackClicked : CitySelectorScreenAction

    data class CitySelected(val city: City) : CitySelectorScreenAction
}
