package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import ru.livetyping.zarina.core.domain.model.geo.City

internal data class NameQueryCities(
    val cityNameQuery: String,
    val cities: List<City>,
)
