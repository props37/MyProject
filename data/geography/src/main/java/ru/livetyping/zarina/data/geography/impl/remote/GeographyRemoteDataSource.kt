package ru.livetyping.zarina.data.geography.impl.remote

import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.common.Location
import ru.livetyping.zarina.core.domain.model.geo.Building
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.model.geo.KladrId
import ru.livetyping.zarina.core.domain.model.geo.Street

internal interface GeographyRemoteDataSource {
    fun getCityByLocationFlow(location: Location): Flow<City>

    fun getCitiesFlow(nameQuery: String?): Flow<List<City>>

    fun getCityStreetsFlow(cityKladrId: KladrId, nameQuery: String): Flow<List<Street>>

    fun getStreetBuildings(streetKladrId: KladrId, nameQuery: String): Flow<List<Building>>
}
