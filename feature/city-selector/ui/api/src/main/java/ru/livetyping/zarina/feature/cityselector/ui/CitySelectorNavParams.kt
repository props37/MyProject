package ru.livetyping.zarina.feature.cityselector.ui

import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable

public data class CitySelectorNavParams(
    val title: Text? = null,
    val currentCity: City? = null,
) {
    public fun toNavEntry(): CitySelectorNavEntry {
        return CitySelectorNavEntry(
            title = title,
            currentCity = currentCity?.let { CityParcelable.from(it) },
        )
    }
}
