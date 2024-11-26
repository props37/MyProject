package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.geo.City

@Stable
internal sealed class CityListItem {
    @Immutable
    data class CityItem(
        val city: City,
        val showFullName: Boolean = false,
    ) : CityListItem()

    @Immutable
    data class CityFirstLetterHeaderItem(val letter: Char) : CityListItem()
}
