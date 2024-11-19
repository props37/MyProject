package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface CitySelectorEvent {
    data object BackClicked : CitySelectorEvent

    data class CityClicked(val city: City) : CitySelectorEvent

    data object ChangeCityClicked : CitySelectorEvent

    data object ErrorRefreshClicked : CitySelectorEvent
}
