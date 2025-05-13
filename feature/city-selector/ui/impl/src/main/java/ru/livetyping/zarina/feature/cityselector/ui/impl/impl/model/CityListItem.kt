package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.geo.City

@Immutable
internal data class CityListItem(
    val city: City,
    val isSelected: Boolean,
)
