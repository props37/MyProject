package ru.livetyping.zarina.data.geography.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.FiasId
import ru.livetyping.zarina.core.domain.model.geo.Street

internal interface GeographyRemoteDataSource {
    fun getCityByLocationFlow(location: Location): Flow<City>

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>>

    fun getCityStreetsFlow(cityFiasId: FiasId, nameQuery: String): Flow<List<Street>>

    fun getStreetBuildings(streetFiasId: FiasId, nameQuery: String): Flow<List<Building>>
}
