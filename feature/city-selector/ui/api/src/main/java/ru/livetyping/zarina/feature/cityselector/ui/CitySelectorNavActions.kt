package ru.livetyping.zarina.feature.cityselector.ui

import ru.livetyping.zarina.core.domain.model.geo.City

public class CitySelectorNavActions(
    public val backClicked: () -> Unit,
    public val citySelected: (City) -> Unit,
)
