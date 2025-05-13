package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface CitySelectorEvent {
    data object CloseClicked : CitySelectorEvent

    data class CitySelected(val city: City) : CitySelectorEvent

    data object RefreshClicked : CitySelectorEvent

    data object SelectCityClicked : CitySelectorEvent
}
