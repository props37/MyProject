package ru.livetyping.zarina.data.geography.impl.local

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.geo.City

internal interface GeographyLocalDataSource {
    fun getCitiesFlow(nameQuery: String?): Flow<List<City>?>

    fun setCities(nameQuery: String?, cities: List<City>)
}
