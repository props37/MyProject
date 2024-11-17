package ru.livetyping.zarina.data.geography.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.City

internal interface GeographyRemoteDataSource {
    fun getCityByLocationFlow(location: Location): Flow<City>

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>>
}
