package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.geo.City

internal sealed interface CityListEvent {
    data class CityClicked(val city: City) : CityListEvent

    data object ChangeCityClicked : CityListEvent

    data object ErrorRefreshClicked : CityListEvent
}
